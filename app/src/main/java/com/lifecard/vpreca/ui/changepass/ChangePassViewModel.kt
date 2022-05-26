package com.lifecard.vpreca.ui.changepass

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.forgotpass.ForgotPassState
import com.lifecard.vpreca.utils.RegexUtils

class ChangePassViewModel : ViewModel() {
    val oldPassError = MutableLiveData<Int?>()
    val newPassError = MutableLiveData<Int?>()
    val cfNewPassError = MutableLiveData<Int?>()
    val validForm = MediatorLiveData<ChangePassState>().apply {
        value = ChangePassState()
        addSource(oldPassError) { value ->
            val previous = this.value
            this.value = previous?.copy(oldPassError = value)
        }
        addSource(newPassError) { value ->
            val previous = this.value
            this.value = previous?.copy(newPassError = value)
        }
        addSource(cfNewPassError) { value ->
            val previous = this.value
            this.value = previous?.copy(cfNewPassError = value)
        }

    }

    fun oldPasswordDataChanged(text: String) {
        if (!RegexUtils.isPasswordValid(text)) {
            oldPassError.value = R.string.rgx_error_password
        } else {
            oldPassError.value = null
        }
    }

    fun newPasswordDataChanged(text: String) {
        if (!RegexUtils.isPasswordValid(text)) {
            newPassError.value = R.string.rgx_error_password
        } else {
            newPassError.value = null
        }
    }

    fun cfNewPasswordDataChanged(text: String, newPass: String) {
        if (!isCfPasswordValid(text, newPass)) {
            cfNewPassError.value = R.string.rgx_error_confirm_password
        } else {
            cfNewPassError.value = null
        }
    }

    private fun isCfPasswordValid(cfPassword: String, newPass: String): Boolean {
        return RegexUtils.isPasswordValid(cfPassword) && cfPassword == newPass
    }
}