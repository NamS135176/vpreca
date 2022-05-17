package com.lifecard.vpreca.ui.changepass

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.forgotpass.ForgotPassState

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
        if (!isPasswordValid(text)) {
            oldPassError.value = R.string.change_pass_old_pass_error
        }
        else{
            oldPassError.value = null
        }
    }
    fun newPasswordDataChanged(text: String) {
        if (!isPasswordValid(text)) {
            newPassError.value = R.string.change_pass_new_pass_error
        }
        else{
            newPassError.value = null
        }
    }
    fun cfNewPasswordDataChanged(text: String, newPass: String) {
        if (!isCfPasswordValid(text,newPass)) {
            cfNewPassError.value = R.string.change_pass_cf_new_pass_error
        }
        else{
            cfNewPassError.value = null
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..12 && isHalfWidth(password)
    }

    private fun isCfPasswordValid(cfPassword: String, newPass: String): Boolean {
        return cfPassword.length in 8..12 && isHalfWidth(cfPassword) &&  cfPassword == newPass
    }

    fun isHalfWidth(text:String):Boolean{
        val regex = "[０-９ぁ-んァ-ン一-龥]".toRegex()
        return regex.find(text) == null
    }
}