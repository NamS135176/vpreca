package com.lifecard.vpreca.ui.forgotpass

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.login.LoginFormState

class ForgotPassViewModel : ViewModel() {
    val emailError = MutableLiveData<Int?>()

    val validForm = MediatorLiveData<ForgotPassState>().apply {
        value = ForgotPassState()
        addSource(emailError) { value ->
            val previous = this.value
            this.value = previous?.copy(emailError = value)
        }
    }

    fun emailDataChanged(text: String) {
        if (!isEmailValid(text)) {
            emailError.value = R.string.forgot_pass_error_email
        } else {
            emailError.value = null
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}