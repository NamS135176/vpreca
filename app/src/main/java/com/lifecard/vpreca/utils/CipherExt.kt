package com.lifecard.vpreca.utils

import android.util.Base64
import com.lifecard.vpreca.data.model.AuthToken
import com.lifecard.vpreca.data.model.toAuthToken
import com.lifecard.vpreca.data.model.toJSON
import java.security.GeneralSecurityException
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
//        val base64 = "o5LnjJOwPoZzxKAfxZyI5xsF670bQfJjrmB7vIH7ZvajEaYy5qKtit4rjvFlVqk7vMeB7wjMxvrV5rlQViuIWrRl9l53ku4GOnh9AEol32E0gVLWK6lm2uccGNtAEF2rWtQkZ10BmsVtnCFzJNOOdmUSskl0tdqIczFBpuMbJODk9QouDRyoQ9p1Lot2iQ5wEtxAoGsT_jsp1xg004q4coT_4C7cXfjRMoTmkZzOlpEZgHLKKkAne-nHIWRsOh4v0nOKY7WgK8LfhB3j8Mmegd_wADUCm066CEMp59qUYEs2vkMdh3u01oCuYYCns_3oWxDhq8LgQ0_M4QmlV_De0ysSDhaGqI0SrJNksTO9MhbhMyOvJ6zxIf_-ICeK-JXeqsBNecpFfjTTjEZKHIRFexj_VayoMmebm2nOtqHkAfomFKtZYAF9r5_yB-LHqSHvrh5-GsGav322iR815whgvlLw_SOZK6ibyY0Iv_y7FCRZN4LURgcssHjYEzbJgLWKX8SmUIEu-BKtLWS8v9M="
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