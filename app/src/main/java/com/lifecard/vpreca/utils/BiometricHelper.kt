package com.lifecard.vpreca.utils

import android.hardware.biometrics.BiometricPrompt
import com.lifecard.vpreca.R

class BiometricHelper {
    companion object {
        fun getMessageIdByErrorCode(errorCode: Int): Int? {
            return when (errorCode) {
                BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT -> R.string.fingerprint_error_lockout_custom
                BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT_PERMANENT -> R.string.fingerprint_error_lockout_custom
                BiometricPrompt.BIOMETRIC_ERROR_UNABLE_TO_PROCESS -> R.string.fingerprint_not_recognized_custom
                else -> null
            }
        }
    }
}