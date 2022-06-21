package com.lifecard.vpreca.utils

import android.util.Base64
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.data.model.toAuthToken
import com.lifecard.vpreca.data.model.toJSON
import javax.crypto.Cipher

fun Cipher.encryptAuthToken(authToken: AuthToken): String? {
    try {
        val encrypted = this.doFinal(authToken.toJSON()?.toByteArray())
        return Base64.encodeToString(encrypted, Base64.URL_SAFE)
    } catch (e: Exception) {
        println("Cipher... encryptAuthToken has exception $e")
    }
    return null
}

fun Cipher.decryptAuthToken(base64: String): AuthToken? {
    try {
        println("Cipher... decryptAuthToken base64 = $base64")
        val decoded = Base64.decode(base64, Base64.URL_SAFE)

        println("Cipher... decryptAuthToken decoded = $decoded")
        val original = this.doFinal(decoded)
        println("Cipher... decryptAuthToken original = ${String(original)}")
        return String(original).toAuthToken()
    } catch (e: Exception) {
        println("Cipher... decryptAuthToken has exception $e")
    }
    return null
}