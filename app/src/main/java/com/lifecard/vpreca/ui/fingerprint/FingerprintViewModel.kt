package com.lifecard.vpreca.ui.fingerprint

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.ui.login.LoginResult
import com.lifecard.vpreca.utils.BiometricHelper
import com.lifecard.vpreca.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.Signature
import javax.inject.Inject

@HiltViewModel
class FingerprintViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _fingerprintSetting = MutableLiveData<Boolean>()
    val fingerprintSetting: LiveData<Boolean> = _fingerprintSetting
    val loading = MutableLiveData<Boolean>(false)
    val registerBiometricResult = MutableLiveData<BioSettingResult>()

    fun setup(context: Context) {
        viewModelScope.let {
            _fingerprintSetting.value =
                PreferenceHelper.isEnableBiometricSetting(appContext = context)
        }
    }

    fun setFingerprintSetting(context: Context, enable: Boolean) {
        _fingerprintSetting.value = enable
        PreferenceHelper.setEnableBiometricSetting(context, enable)
    }

    fun checkSupportFingerprint(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> return true
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                registerBiometricResult.value =
//                    BioSettingResult(
//                        error = R.string.error_fingerprint_not_enrolled,
//                        bioStatus = BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
//                    )
                return false
            }
            else -> {
//                registerBiometricResult.value =
//                    BioSettingResult(error = R.string.error_fingerprint_not_support)
                return false
            }
        }
    }

    fun uploadPublicKey(publicKey: String, signature: Signature? = null) {
        viewModelScope.launch {
            println("uploadPublicKey... start")
            loading.value = true
//            registerBiometricResult.value = null
            val bioChallengeResult =
                remoteRepository.registerBiometric(userManager.loginId!!, publicKey)

            if (bioChallengeResult is Result.Success) {
                registerBiometricResult.value = BioSettingResult(success = bioChallengeResult.data)

                //testing
                /*
                signature?.let {
//                    testSignature(signature, publicKey, bioChallengeResult.data)
                    val bioChallenge = bioChallengeResult.data
                    val challenge = bioChallenge.challenge
                    val salt = bioChallenge.salt
                    val nonce = bioChallenge.nonce

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
                    val respByte =
                        Base64.decode(signedBySignature, Base64.URL_SAFE or Base64.NO_WRAP)
                    val verify = verificationFunction.verify(respByte)
                    println("uploadSignature... stringToSign = $stringToSign")
                    println("uploadSignature... signedBySignature = $signedBySignature")
                    println("uploadSignature... verify result = $verify")
                    println("uploadSignature... publicKey = $publicKey")
                }
                 */
                //end test

            } else {
                registerBiometricResult.value
                BioSettingResult(error = R.string.error_bio_authentication_failure)
            }
            loading.value = false

            println("uploadPublicKey... end")
        }
    }

    fun handleAuthenticationError(
        errorCode: Int,
        errString: CharSequence
    ) {
        println("handleAuthenticationError... errorCode=$errorCode errString=$errString")
        val messageResId = BiometricHelper.getMessageIdByErrorCode(errorCode)
        messageResId?.let {
            registerBiometricResult.value = BioSettingResult(
                error = it
            )
        } ?: kotlin.run {
//            _loginResult.value =
//                LoginResult(errorText = errString.toString())
        }
    }
}
