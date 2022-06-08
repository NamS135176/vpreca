package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.ui.signup.PhoneState
import com.lifecard.vpreca.utils.RegexUtils

class ChangePhoneInputPhoneViewModel : ViewModel() {
    val phoneError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(PhoneState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private fun checkPhoneValid(): Boolean {
        return if (!RegexUtils.isPhoneNumberValid(formState.value?.phone)) {
            phoneError.value = R.string.rgx_error_phone_number
            false
        } else {
            phoneError.value = null
            true
        }
    }

    fun phoneDataChanged(text: String) {
        formState.value = formState.value?.copy(phone = text)
    }

    fun submit() {
        val isPhoneValid = checkPhoneValid()
        if(isPhoneValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.phone)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }
}