package com.lifecard.vpreca.biometric

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import java.security.KeyStore
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

interface BioManager {
    fun getPublicKey(): String?
    fun getInitializedCipherForEncryption(): Cipher
    fun getInitializedCipherForDecryption(): Cipher
    fun getCryptoObjectForPromptBio(
        cipherMode: Int,
        forceDeleteIfError: Boolean? = false
    ): BiometricPrompt.CryptoObject?

    fun getSupportBiometricType(): BiometricType
    fun getBiometricTypeForAuthentication(): BiometricType
    fun checkDeviceSupportBiometric(): Boolean
    fun clearCache()
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
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_NAME = "vpreca_bio_authentication_key"
        private const val CIPHER_ALGORITHM_AES =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"

        @JvmStatic
        var authenticatedEncryptCipher: Cipher? = null
    }

    private val biometricManager: BiometricManager by lazy { BiometricManager.from(context) }
    private val availableFeatures: List<BiometricType> by lazy {
        listOf(
            "android.hardware.biometrics.face" to BiometricType.FACE,//PackageManager.FEATURE_FACE
            "android.hardware.fingerprint" to BiometricType.FINGERPRINT,//PackageManager.FEATURE_FINGERPRINT
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
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
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

    override fun getInitializedCipherForEncryption(): Cipher {
        val secretKey = getOrCreateKey()
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, CIPHER_ALGORITHM_AES.toByteArray(), 0, 12)
        )
        return cipher
    }

    override fun getInitializedCipherForDecryption(): Cipher {
        val secretKey = getOrCreateKey()
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES)
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, CIPHER_ALGORITHM_AES.toByteArray(), 0, 12)
        )
        return cipher
    }

    override fun getCryptoObjectForPromptBio(
        cipherMode: Int,
        forceDeleteIfError: Boolean?
    ): BiometricPrompt.CryptoObject? {
        return try {
            if (forceDeleteIfError == true) {
                keyStore.load(null) // Keystore must be loaded before it can be accessed
                keyStore.deleteEntry(KEY_NAME)
            }
            val cipher =
                if (cipherMode == Cipher.ENCRYPT_MODE) getInitializedCipherForEncryption() else getInitializedCipherForDecryption()
            BiometricPrompt.CryptoObject(cipher)
        } catch (e: KeyPermanentlyInvalidatedException) {
            println(e)
            return getCryptoObjectForPromptBio(cipherMode, true)
        } catch (e: Exception) {
            println(e)
            null
        }
    }


    private fun getOrCreateKey(): SecretKey? {
        val keyName = KEY_NAME
        // If Secretkey was previously created for that keyName, then grab and return it.
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            //new for secret key
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setRandomizedEncryptionRequired(false)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        keyGenerator.generateKey()

        return keyStore.getKey(keyName, null)?.let { return it as SecretKey }
    }

    private fun PublicKey.toPEM() = "-----BEGIN PUBLIC KEY-----\n${
        String(
            this.encoded.encodeBase64(),
            Charsets.US_ASCII
        )
    }-----END PUBLIC KEY-----"

    private fun ByteArray.encodeBase64(): ByteArray = Base64.encode(this, Base64.DEFAULT)

    override fun clearCache() {
        authenticatedEncryptCipher = null
    }
}