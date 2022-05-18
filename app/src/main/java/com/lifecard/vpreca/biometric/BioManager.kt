package com.lifecard.vpreca.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import java.security.*
import java.security.spec.ECGenParameterSpec

interface BioManager {
    fun getPublicKey(): String?
    fun getInitializedSignatureForEncryption(): Signature
    fun getCryptoObjectForPromtBio(): BiometricPrompt.CryptoObject?
}

@RequiresApi(Build.VERSION_CODES.M)
class BioManagerImpl : BioManager {
    //    private val KEY_SIZE = 256
    private val ANDROID_KEYSTORE = "AndroidKeyStore"

    //    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
//    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_EC
    private val ENCRYPTION_SIGNATURE_ALGORITHM = "SHA256withECDSA"
    private val KEY_NAME = "vpreca_bio_authentication_key"

    private val keyStore: KeyStore by lazy { KeyStore.getInstance(ANDROID_KEYSTORE) }

    override fun getPublicKey(): String? {
        return try {
            val secretKey = getOrCreateKey()
            val aliases = keyStore.aliases()
            print("getPublicKey aliases = $aliases")
            while (aliases.hasMoreElements()) {
                val alias = aliases.nextElement()
                print("getPublicKey alias = $alias")
                val cert = keyStore.getCertificate(alias)
                print("getPublicKey cert = ${cert?.publicKey?.toPEM()}")
            }
            print("getPublicKey secretKey = $secretKey")
            keyStore.load(null) // Keystore must be loaded before it can be accessed
            val certificate = keyStore.getCertificate(KEY_NAME)
            val publicKey = certificate.publicKey
            publicKey.toPEM()
        } catch (e: Exception) {
            null
        }
    }

    override fun getInitializedSignatureForEncryption(): Signature {
        val signature = Signature.getInstance(ENCRYPTION_SIGNATURE_ALGORITHM)
        val privateKey = getOrCreateKey()
        signature.initSign(privateKey)
        return signature
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getCryptoObjectForPromtBio(): BiometricPrompt.CryptoObject? {
        return try {
            val signature = getInitializedSignatureForEncryption()
            BiometricPrompt.CryptoObject(signature)
        } catch (e: Exception) {
            null
        }
    }

    private fun getOrCreateKey(): PrivateKey? {
        val keyName = KEY_NAME
        // If Secretkey was previously created for that keyName, then grab and return it.
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(keyName, null)?.let { return it as PrivateKey }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            KeyProperties.PURPOSE_SIGN
        )
        paramsBuilder.apply {
            setDigests(KeyProperties.DIGEST_SHA256)
            setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
//            setBlockModes(ENCRYPTION_BLOCK_MODE)
//            setEncryptionPaddings(ENCRYPTION_PADDING)
//            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setInvalidatedByBiometricEnrollment(true)
            }
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyPairGenerator.getInstance(
            ENCRYPTION_ALGORITHM,
            ANDROID_KEYSTORE
        )
        keyGenerator.initialize(keyGenParams)
        keyGenerator.generateKeyPair()

        return keyStore.getKey(keyName, null)?.let { return it as PrivateKey }
    }

    private fun PublicKey.toPEM() = "-----BEGIN PUBLIC KEY-----\n${
        String(
            this.encoded.encodeBase64(),
            Charsets.US_ASCII
        )
    }\n-----END PUBLIC KEY-----"

    private fun ByteArray.encodeBase64(): ByteArray = Base64.encode(this, Base64.DEFAULT)
}