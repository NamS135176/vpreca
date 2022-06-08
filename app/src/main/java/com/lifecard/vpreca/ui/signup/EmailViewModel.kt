package com.lifecard.vpreca.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.utils.RegexUtils

class EmailViewModel : ViewModel() {
    val emailError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(EmailState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private fun checkEmailValid(): Boolean {
        return if (!RegexUtils.isEmailValid(formState.value?.email)) {
            emailError.value = R.string.rgx_error_email
            false
        } else {
            emailError.value = null
            true
        }
    }

    fun emailDataChanged(text: String) {
        formState.value = formState.value?.copy(email = text)
    }

    fun submit() {
        val isEmailValid = checkEmailValid()
        if(isEmailValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.email)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }

}