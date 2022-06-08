package com.lifecard.vpreca.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState

class ConfirmEmailViewModel:ViewModel() {
    val cfEmailError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ConfirmEmailState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private fun checkCfEmailValid(): Boolean {
        return if (formState.value?.confirmEmail?.length != 4) {
            cfEmailError.value = R.string.error_code
            false
        } else {
            cfEmailError.value = null
            true
        }
    }

    fun cfEmailDataChanged(text: String) {
        formState.value = formState.value?.copy(confirmEmail = text)
    }

    fun submit() {
        val isCfPhoneValid = checkCfEmailValid()
        if(isCfPhoneValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.confirmEmail)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }
}