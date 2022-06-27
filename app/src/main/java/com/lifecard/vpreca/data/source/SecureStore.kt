package com.lifecard.vpreca.data.source

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.data.model.toAuthToken
import com.lifecard.vpreca.data.model.toJSON
import com.lifecard.vpreca.utils.Constant
import com.lifecard.vpreca.utils.PreferenceHelper
import com.lifecard.vpreca.utils.decryptAuthToken
import com.lifecard.vpreca.utils.encryptAuthToken
import javax.crypto.Cipher

interface AuthTokenStore {
    fun saveAuthToken(authToken: AuthToken)
    fun getAuthToken(): AuthToken?
    fun clear()
}

internal class BioAuthTokenStore(
    appContext: Context,
    var encryptCipher: Cipher? = null,
    var decryptCipher: Cipher? = null
) : AuthTokenStore {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("VPrecaBioSecureStorePref", Context.MODE_PRIVATE)

    private fun saveEncryptText(encryptedText: String) {
        with(sharedPreferences.edit()) {
            putString(Constant.SECURE_AUTH_TOKEN, encryptedText)
            apply()
        }
    }

    override fun saveAuthToken(authToken: AuthToken) {
        try {
            val base64 = encryptCipher?.encryptAuthToken(authToken = authToken)
            base64?.let { saveEncryptText(base64) }
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun getAuthToken(): AuthToken? {
        try {
            val encryptedText = sharedPreferences.getString(Constant.SECURE_AUTH_TOKEN, null)
            return encryptedText?.let { decryptCipher?.decryptAuthToken(encryptedText) }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override fun clear() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}

internal class NormalAuthTokenStore(private val appContext: Context) : AuthTokenStore {

    private val encryptedSharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("VPrecaSecurePref", Context.MODE_PRIVATE)

    private fun saveEncryptText(key: String, textToEncrypt: String) {
        with(encryptedSharedPreferences.edit()) {
            val encryptedValue = EncryptionUtils.encrypt(appContext, textToEncrypt)
            putString(key, encryptedValue)
            apply()
        }
    }

    private fun getEncryptText(key: String): String? {
        val encryptedValue = encryptedSharedPreferences.getString(key, null)
        return EncryptionUtils.decrypt(appContext, encryptedValue)
    }

    override fun saveAuthToken(authToken: AuthToken) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            authToken.toJSON()?.let { saveEncryptText(Constant.SECURE_AUTH_TOKEN, it) }
        } else {
            with(encryptedSharedPreferences.edit()) {
                putString("${Constant.SECURE_AUTH_TOKEN}_accessToken", EncryptionUtils.encrypt(appContext, authToken.accessToken))
                putString("${Constant.SECURE_AUTH_TOKEN}_refreshToken", EncryptionUtils.encrypt(appContext, authToken.refreshToken))
                putString("${Constant.SECURE_AUTH_TOKEN}_memberNumber", EncryptionUtils.encrypt(appContext, authToken.memberNumber))
                putString("${Constant.SECURE_AUTH_TOKEN}_loginId", EncryptionUtils.encrypt(appContext, authToken.loginId))
                apply()
            }
        }
    }

    override fun getAuthToken(): AuthToken? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getEncryptText(Constant.SECURE_AUTH_TOKEN)?.toAuthToken()
        } else {
            val accessToken = getEncryptText("${Constant.SECURE_AUTH_TOKEN}_accessToken")
            val refreshToken = getEncryptText("${Constant.SECURE_AUTH_TOKEN}_refreshToken")
            val memberNumber = getEncryptText("${Constant.SECURE_AUTH_TOKEN}_memberNumber")
            val loginId = getEncryptText("${Constant.SECURE_AUTH_TOKEN}_loginId")
            if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
                && !memberNumber.isNullOrEmpty() && !loginId.isNullOrEmpty()
            ) {
                return AuthToken(accessToken, refreshToken, memberNumber, loginId)
            }
            return null
        }
    }

    override fun clear() {
        with(encryptedSharedPreferences.edit()) {
            clear()
            apply()
        }
        EncryptionUtils.clear()
    }
}

class SecureStore(private val appContext: Context) {

    private var bioAuthTokenStore: BioAuthTokenStore? = null
    private var normalAuthToken: NormalAuthTokenStore = NormalAuthTokenStore(appContext)

    fun updateEncryptBioAuthTokenStore(encryptCipher: Cipher) {
        bioAuthTokenStore?.let { it.encryptCipher = encryptCipher }
            ?: kotlin.run {
                bioAuthTokenStore = BioAuthTokenStore(appContext, encryptCipher = encryptCipher)
            }
    }

    fun updateDecryptBioAuthTokenStore(decryptCipher: Cipher) {
        bioAuthTokenStore?.let { it.decryptCipher = decryptCipher }
            ?: kotlin.run {
                bioAuthTokenStore = BioAuthTokenStore(appContext, decryptCipher = decryptCipher)
            }
    }

    fun saveAuthToken(context: Context, authToken: AuthToken) {
        val isEnableBiometricSetting = PreferenceHelper.isEnableBiometricSetting(context)
        if (isEnableBiometricSetting) {
            bioAuthTokenStore?.let {
                it.saveAuthToken(authToken)
                normalAuthToken.clear()//clear normal for safe
            }
        } else {
            normalAuthToken.saveAuthToken(authToken)
        }
    }

    fun getAuthToken(): AuthToken? {
        return bioAuthTokenStore?.getAuthToken() ?: normalAuthToken.getAuthToken()
    }

    fun moveAuthTokenToNormalStore(authToken: AuthToken) {
        bioAuthTokenStore?.clear()
        bioAuthTokenStore = null
        normalAuthToken.saveAuthToken(authToken)
    }


    fun clear() {
        bioAuthTokenStore?.clear()
        normalAuthToken.clear()
    }
}