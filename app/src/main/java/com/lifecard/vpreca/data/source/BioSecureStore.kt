package com.lifecard.vpreca.data.source

import android.content.Context
import android.content.SharedPreferences
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.utils.encryptAuthToken
import javax.crypto.Cipher

class BioSecureStore(
    private val appContext: Context,
    private val encryptCipher: Cipher?,
    private val decryptCipher: Cipher?
) : AuthTokenStore {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("VPrecaBioSecureStorePref", Context.MODE_PRIVATE)

    private fun saveEncryptText(encryptedText: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", encryptedText)
            apply()
        }
    }

    override fun saveAuthToken(authToken: AuthToken) {
        try {
            val base64 = encryptCipher?.encryptAuthToken(authToken = authToken)
            saveEncryptText(base64)
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun getAuthToken(): AuthToken {
        try {
            val base64 = encryptCipher?.encryptAuthToken(authToken = authToken)
            saveEncryptText(base64)
        } catch (e: Exception) {
            println(e)
        }
    }
}