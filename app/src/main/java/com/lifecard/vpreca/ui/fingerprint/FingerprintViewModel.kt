package com.lifecard.vpreca.ui.fingerprint

import android.app.Application
import androidx.biometric.BiometricManager
import androidx.lifecycle.*
import com.lifecard.vpreca.utils.PreferenceHelper

class FingerprintViewModel(private var app: Application) : AndroidViewModel(app) {

    private val _fingerprintSetting = MutableLiveData<Boolean>()
    val fingerprintSetting: LiveData<Boolean> = _fingerprintSetting

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
}