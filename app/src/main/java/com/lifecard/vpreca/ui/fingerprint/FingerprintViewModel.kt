package com.lifecard.vpreca.ui.fingerprint

import android.app.Application
import android.os.Build
import android.os.Handler
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.utils.PreferenceHelper
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import com.lifecard.vpreca.data.Result

class FingerprintViewModel(private var app: Application) : AndroidViewModel(app) {

    private val _fingerprintSetting = MutableLiveData<Boolean>()
    val fingerprintSetting: LiveData<Boolean> = _fingerprintSetting
    val loading = MutableLiveData<Boolean>(false)
    val registerBiometricResult = MutableLiveData<Result<Boolean>?>()

    init {
        viewModelScope.let {
            _fingerprintSetting.value =
                PreferenceHelper.isFingerprintSetting(appContext = app)
        }
    }

    fun setFingerprintSetting(enable: Boolean) {
        _fingerprintSetting.value = enable
        PreferenceHelper.setFingerprintSetting(app, enable)
    }

    fun checkSupportFingerprint(): Boolean {
        val biometricManager = BiometricManager.from(app)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> return true
            else -> return false
        }
    }

    fun uploadSignature(signature: Signature) {
        val challenge = "abc"
        val salt = "salt"
        val nonce = "nonce"

        val stringToSign = "$challenge$salt$nonce"
        signature.update(stringToSign.toByteArray())
        val signatureBytes = signature.sign()
        val signed = Base64.encodeToString(signatureBytes, Base64.URL_SAFE or Base64.NO_WRAP)
        print("uploadSignature... signed = $signed")
    }

    fun uploadCipher(cipher: Cipher) {
        val challenge = "abc"
        val salt = "salt"
        val nonce = "nonce"

        val stringToSign = "$challenge$salt$nonce"
        val signatureBytes = cipher.doFinal(stringToSign.toByteArray())
        val signed = Base64.encodeToString(signatureBytes, Base64.URL_SAFE or Base64.NO_WRAP)
        print("uploadCipher... signed = $signed")
    }

    fun uploadPublicKey(publicKey: String) {
        loading.value = true
        registerBiometricResult.value = null
        print("uploadPublicKey... publicKey = $publicKey")
        Handler().postDelayed(Runnable {
            loading.value = false
            registerBiometricResult.value = Result.Success(true)
        }, 500)
    }

    fun testSignature(signature: Signature, publicKey: String) {
        val challenge = "challenge"
        val salt = "salt"
        val nonce = "nonce"

        val stringToSign = "$challenge$salt$nonce"
        signature.update(stringToSign.toByteArray())
        val signatureBytes = signature.sign()
        val signedBySignature =
            Base64.encodeToString(signatureBytes, Base64.URL_SAFE or Base64.NO_WRAP)

        var pk = publicKey
        pk = pk?.replace("-----BEGIN PUBLIC KEY-----\n", "")
        pk = pk?.replace("-----END PUBLIC KEY-----", "")
        pk = pk?.replace("\n", "")

        val pkb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getDecoder().decode(pk)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val fact = KeyFactory.getInstance("EC")
        var pkKey = fact.generatePublic(X509EncodedKeySpec(pkb))

        val verificationFunction = Signature.getInstance("SHA256withECDSA")
        verificationFunction.initVerify(pkKey)
        verificationFunction.update(stringToSign.toByteArray())
        val respByte = Base64.decode(signedBySignature, Base64.URL_SAFE or Base64.NO_WRAP)
        val verify = verificationFunction.verify(respByte)
        print("uploadSignature... stringToSign = $stringToSign - signedBySignature = $signedBySignature - verify = $verify")
    }
}