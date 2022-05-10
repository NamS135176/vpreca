package com.lifecard.vpreca.ui.signup

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.login.LoginFormState

class SignupInputViewModel : ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val idError = MutableLiveData<Int?>()

    val validForm = MediatorLiveData<SignUpFormState>().apply {
        value = SignUpFormState()
        addSource(idError) { value ->
            val previous = this.value
            this.value = previous?.copy(idError = value)
        }
        addSource(usernameError) { value ->
            val previous = this.value
            this.value = previous?.copy(usernameError = value)
        }

    }

    fun usernameDataChanged(text: String) {
        if (!isUserNameValid(text)) {
            usernameError.value = R.string.invalid_username
        } else {
            usernameError.value = null
        }
    }

    fun idDataChanged(text: String) {
        if (!isIdValid(text)) {
            idError.value = R.string.invalid_password
        } else {
            idError.value = null
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length >= 2 && username.length<=18
    }

    // A placeholder password validation check
    private fun isIdValid(id: String): Boolean {
        return id.length >= 6 && id.length<=10
    }
}