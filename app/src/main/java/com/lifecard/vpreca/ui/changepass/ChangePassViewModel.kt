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
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ChangePassState())

    private fun checkOldPassValid(): Boolean {
        return if (!RegexUtils.isPasswordValid(formState.value?.oldPass)) {
            oldPassError.value = R.string.rgx_error_password
            false
        } else {
            oldPassError.value = null
            true
        }
    }

    fun oldPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(oldPass = text)
    }

    private fun checkNewPassValid(): Boolean {
        return if (!RegexUtils.isPasswordValid(formState.value?.newPass)) {
            newPassError.value = R.string.rgx_error_password
            false
        } else {
            newPassError.value = null
            true
        }
    }

    fun newPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(newPass = text)
    }

    private fun checkCfNewPassValid(): Boolean {
        return if (!isCfPasswordValid(formState.value?.cfNewPass, formState.value?.newPass)) {
            cfNewPassError.value = R.string.rgx_error_confirm_password
            false
        } else {
            cfNewPassError.value = null
            true
        }
    }

    fun cfNewPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(cfNewPass = text)
    }

    private fun isCfPasswordValid(cfPassword: String?, newPass: String?): Boolean {
        return RegexUtils.isPasswordValid(cfPassword) && cfPassword == newPass
    }

    fun submit() {

    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.oldPass, form.newPass, form.cfNewPass)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }
}