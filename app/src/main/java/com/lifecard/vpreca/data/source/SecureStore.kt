package com.lifecard.vpreca.data.source

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.lifecard.vpreca.utils.Constanst

class SecureStore(private val appContext: Context) {

    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        Constanst.ENCRYPTED_SHARED_PREFS_FILENAME,
        Constanst.MASTER_KEY_ALIAS,
        appContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveEncryptText(key: String, textToEncrypt: String) {
        with(encryptedSharedPreferences.edit()) {
            putString(key, textToEncrypt)
            apply()
        }
    }

    fun getEncryptText(key: String): String? {
        return encryptedSharedPreferences.getString(key, null)
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
}