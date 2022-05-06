package com.lifecard.vpreca.ui.login

import android.util.Patterns
import androidx.lifecycle.*

import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: UserRepository) : ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val passwordError = MutableLiveData<Int?>()
    val validForm = MediatorLiveData<LoginFormState>().apply {
        value = LoginFormState()
        addSource(usernameError) { value ->
            val previous = this.value
            this.value = previous?.copy(usernameError = value)
        }
        addSource(passwordError) { value ->
            val previous = this.value
            this.value = previous?.copy(passwordError = value)
        }
    }

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = result.data)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

//    fun loginDataChanged(username: String, password: String) {
////        _loginForm.value?.
//        if (!isUserNameValid(username)) {
//            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
//        } else if (!isPasswordValid(password)) {
//            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
//        } else {
//            _loginForm.value = LoginFormState(isDataValid = true)
//        }
//    }

    fun usernameDataChanged(text: String) {
        if (!isUserNameValid(text)) {
            usernameError.value = R.string.invalid_username
        } else {
            usernameError.value = null
        }
    }

    fun passwordDataChanged(text: String) {
        if (!isPasswordValid(text)) {
            passwordError.value = R.string.invalid_password
        } else {
            passwordError.value = null
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}