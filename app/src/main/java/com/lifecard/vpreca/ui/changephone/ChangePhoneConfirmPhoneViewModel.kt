package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.ui.signup.ConfirmPhoneState

class ChangePhoneConfirmPhoneViewModel : ViewModel() {
    val cfPhoneError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ConfirmPhoneState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private fun checkCfPhoneValid(): Boolean {
        return if (formState.value?.confirmCode?.length != 4) {
            cfPhoneError.value = R.string.error_code
            false
        } else {
            cfPhoneError.value = null
            true
        }
    }

    fun cfPhoneDataChanged(text: String) {
        formState.value = formState.value?.copy(confirmCode = text)
    }

    fun submit() {
        val isCfPhoneValid = checkCfPhoneValid()
        if(isCfPhoneValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.confirmCode)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }
}