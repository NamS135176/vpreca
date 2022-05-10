package com.lifecard.vpreca.data.source

import android.content.Context
import android.content.SharedPreferences
import com.lifecard.vpreca.utils.Constanst

class SecureStore(private val appContext: Context) {

    private val encryptedSharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("VPrecaPref", Context.MODE_PRIVATE)

    private fun saveEncryptText(key: String, textToEncrypt: String) {
        with(encryptedSharedPreferences.edit()) {
            val encryptedValue = EncryptionUtils.encrypt(appContext, textToEncrypt)
            putString(key, encryptedValue)
            apply()
        }
    }

    fun getEncryptText(key: String): String? {
        val encryptedValue = encryptedSharedPreferences.getString(key, null)
        return EncryptionUtils.decrypt(appContext, encryptedValue)
    }

    fun saveAccessToken(token: String) {
        return saveEncryptText(Constanst.SECURE_ACCESS_TOKEN, token)
    }

    fun getAccessToken(): String? {
        return getEncryptText(Constanst.SECURE_ACCESS_TOKEN)
    }

    fun saveRefreshToken(token: String) {
        return saveEncryptText(Constanst.SECURE_REFRESH_TOKEN, token)
    }

    fun getRefreshToken(): String? {
        return getEncryptText(Constanst.SECURE_REFRESH_TOKEN)
    }

    fun clear() {
        with(encryptedSharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}