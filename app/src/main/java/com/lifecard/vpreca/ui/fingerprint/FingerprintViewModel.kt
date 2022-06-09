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
