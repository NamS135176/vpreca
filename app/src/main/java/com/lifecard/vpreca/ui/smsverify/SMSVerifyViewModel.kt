package com.lifecard.vpreca.ui.smsverify

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.forgotpass.ForgotPassState

class SMSVerifyViewModel : ViewModel() {
    val codeError = MutableLiveData<Int?>()

    val validForm = MediatorLiveData<SMSVerifyState>().apply {
        value = SMSVerifyState()
        addSource(codeError) { value ->
            val previous = this.value
            this.value = previous?.copy(codeError = value)
        }
    }

    fun codeDataChanged(text: String) {
        if (!isCodeValid(text)) {
            codeError.value = androidx.appcompat.R.id.none
        } else {
            codeError.value = null
        }
    }

    // A placeholder username validation check
    private fun isCodeValid(code: String): Boolean {
        return code.isNotBlank()
    }

}