package com.lifecard.vpreca.utils

import android.util.Base64
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.data.model.toAuthToken
import com.lifecard.vpreca.data.model.toJSON
import java.security.GeneralSecurityException
import javax.crypto.Cipher

fun Cipher.encryptAuthToken(authToken: AuthToken): String? {
    try {
        val encrypted = this.doFinal(authToken.toJSON().toByteArray())
        return Base64.encodeToString(encrypted, Base64.URL_SAFE)
    } catch (e: GeneralSecurityException) {
        print("Cipher... encryptAuthToken has exception $e")
    }
    return null
}

fun Cipher.decryptAuthToken(encryptedJsonAuthToken: String): AuthToken? {
    try {
        val decoded = Base64.decode(encryptedJsonAuthToken, Base64.URL_SAFE)
        val original = this.doFinal(decoded)
        return String(original).toAuthToken()
    } catch (e: GeneralSecurityException) {
        print("Cipher... decryptAuthToken has exception $e")
    }
    return null
}