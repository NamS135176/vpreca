package com.lifecard.vpreca.data.api

import android.util.Base64
import com.google.android.gms.common.util.Hex
import com.google.gson.Gson
import com.lifecard.vpreca.BuildConfig
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

class MessageDigest {
    private val gson = Gson()
//
    private val privateKeyPem = BuildConfig.privateKeyPem

    private val privateKey = privateKeyPem.replace("-----BEGIN RSA PRIVATE KEY-----", "")
        .replace("-----END RSA PRIVATE KEY-----", "")
        .replace(Regex("[\\n]"), "")

    fun sign(textToSign: String): String? {
        try {
            val keyBytes: ByteArray = Base64.decode(privateKey, Base64.DEFAULT)
            val spec = PKCS8EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("RSA")

            val privateSignature = Signature.getInstance("SHA256withRSA")
            privateSignature.initSign(kf.generatePrivate(spec))
            privateSignature.update(textToSign.toByteArray(Charsets.UTF_8))
            val signed: ByteArray = privateSignature.sign()
            val md = MessageDigest.getInstance("SHA-256")
            val digest: ByteArray = md.digest(signed)
            return Hex.bytesToStringLowercase(digest)
        } catch (e: Exception) {
            println(e)
            return null
        }
    }

    fun sign(data: Any): String? {
        return sign(gson.toJson(data))
    }
}