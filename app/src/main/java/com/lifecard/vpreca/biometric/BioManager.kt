package com.lifecard.vpreca.biometric

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import java.security.*
import java.security.spec.ECGenParameterSpec

interface BioManager {
    fun getPublicKey(): String?
    fun getInitializedSignatureForEncryption(): Signature
    fun getCryptoObjectForPromtBio(forceDeleteIfError: Boolean? = false): BiometricPrompt.CryptoObject?
    fun getSupportBiometricType(): BiometricType
    fun getBiometricTypeForAuthentication(): BiometricType
    fun checkDeviceSupportBiometric(): Boolean
}

enum class BiometricType {
    FACE,
    FINGERPRINT,
    IRIS,
    MULTIPLE,
    NONE
}

@RequiresApi(Build.VERSION_CODES.M)
class BioManagerImpl constructor(private val context: Context) : BioManager {

    companion object {
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_EC
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ENCRYPTION_SIGNATURE_ALGORITHM = "SHA256withECDSA"
        private const val KEY_NAME = "vpreca_bio_authentication_key"
    }

    private val biometricManager: BiometricManager by lazy { BiometricManager.from(context) }
    private val availableFeatures: List<BiometricType> by lazy {
        listOf(
            "android.hardware.biometrics.face" to BiometricType.FACE,//PackageManager.FEATURE_FACE
            "android.hardware.fingerprint" to BiometricType.FINGERPRINT,//PackageManager.FEATURE_FINGERPRINT
            "android.hardware.biometrics.iris" to BiometricType.IRIS,//PackageManager.FEATURE_IRIS
        ).filter { context.packageManager.hasSystemFeature(it.first) }.map { it.second }
    }

    private val keyStore: KeyStore by lazy { KeyStore.getInstance(ANDROID_KEYSTORE) }

    override fun getSupportBiometricType(): BiometricType {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> when {
                availableFeatures.isEmpty() -> BiometricType.NONE
                availableFeatures.size == 1 -> availableFeatures[0]
                else -> BiometricType.MULTIPLE
            }
            else -> BiometricType.NONE
        }
    }

    override fun getBiometricTypeForAuthentication(): BiometricType {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> when {
                availableFeatures.isEmpty() -> BiometricType.NONE
                availableFeatures.size == 1 -> availableFeatures[0]
                else -> {
                    if (availableFeatures.contains(BiometricType.FACE)) {
                        return BiometricType.FACE
                    } else if (availableFeatures.contains(BiometricType.FINGERPRINT)) {
                        return BiometricType.FINGERPRINT
                    }
                    return BiometricType.NONE
                }
            }
            else -> BiometricType.NONE
        }
    }

    override fun checkDeviceSupportBiometric(): Boolean =
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> true
            else -> false
        }

    override fun getPublicKey(): String? {
        return try {
            getOrCreateKey()
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
    override fun getCryptoObjectForPromtBio(forceDeleteIfError: Boolean?): BiometricPrompt.CryptoObject? {
        return try {
            if (forceDeleteIfError == true) {
                keyStore.load(null) // Keystore must be loaded before it can be accessed
                keyStore.deleteEntry(KEY_NAME)
            }
            val signature = getInitializedSignatureForEncryption()
            BiometricPrompt.CryptoObject(signature)
        } catch (e: KeyPermanentlyInvalidatedException) {
            return getCryptoObjectForPromtBio(true)
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
            KeyProperties.PURPOSE_SIGN
        )
        paramsBuilder.apply {
            setDigests(KeyProperties.DIGEST_SHA256)
            setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
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
    }-----END PUBLIC KEY-----"

    private fun ByteArray.encodeBase64(): ByteArray = Base64.encode(this, Base64.DEFAULT)
}