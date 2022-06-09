package com.lifecard.vpreca.data.source

import android.content.Context
import android.content.SharedPreferences
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.data.model.toAuthToken
import com.lifecard.vpreca.data.model.toJSON
import com.lifecard.vpreca.utils.Constant
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
            commit()
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
        authToken.toJSON()?.let { saveEncryptText(Constant.SECURE_AUTH_TOKEN, it) }
    }

    override fun getAuthToken(): AuthToken? {
        return getEncryptText(Constant.SECURE_AUTH_TOKEN)?.toAuthToken()
    }

    override fun clear() {
        with(encryptedSharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}

class SecureStore(private val appContext: Context) : AuthTokenStore {

    private var bioAuthTokenStore: BioAuthTokenStore? = null
    private var normalAuthToken: NormalAuthTokenStore = NormalAuthTokenStore(appContext)

    private val encryptedSharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("VPrecaSecureStorePref", Context.MODE_PRIVATE)

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
        bioAuthTokenStore?.let {
            it.saveAuthToken(authToken)
            normalAuthToken.clear()//clear normal for safe
        }
            ?: normalAuthToken.saveAuthToken(authToken)
    }

    override fun getAuthToken(): AuthToken? {
        return bioAuthTokenStore?.getAuthToken() ?: normalAuthToken.getAuthToken()
    }

    fun moveAuthTokenToNormalStore(authToken: AuthToken) {
        bioAuthTokenStore?.clear()
        bioAuthTokenStore = null
        normalAuthToken.saveAuthToken(authToken)
    }


    override fun clear() {
        bioAuthTokenStore?.clear()
        normalAuthToken.clear()

        with(encryptedSharedPreferences.edit()) {
            clear()
            commit()
        }
    }

    fun saveAccessToken(token: String) {
        return saveEncryptText(Constant.SECURE_ACCESS_TOKEN, token)
    }

    fun getAccessToken(): String? {
        return getEncryptText(Constant.SECURE_ACCESS_TOKEN)
    }

    fun saveRefreshToken(token: String) {
        return saveEncryptText(Constant.SECURE_REFRESH_TOKEN, token)
    }

    fun getRefreshToken(): String? {
        return getEncryptText(Constant.SECURE_REFRESH_TOKEN)
    }

    fun saveLoginUserId(userId: String) {
        return saveEncryptText(Constant.SECURE_USER_ID, userId)
    }

    fun getLoginUserId(): String? {
        return getEncryptText(Constant.SECURE_USER_ID)
    }

    fun saveMemberNumber(userId: String) {
        return saveEncryptText(Constant.SECURE_MEMBER_NUMBER, userId)
    }

    fun getMemberNumber(): String? {
        return getEncryptText(Constant.SECURE_MEMBER_NUMBER)
    }

    fun clearDueLogout() {
        val useId = getLoginUserId()
        with(encryptedSharedPreferences.edit()) {
            clear()
            commit()
        }
        useId?.let { saveLoginUserId(useId) }
    }
}