package com.lifecard.vpreca.ui.login

import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.util.Base64
import android.util.Patterns
import androidx.lifecycle.*

import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.LoginAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.Signature
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: UserRepository,
    private val remoteRepository: RemoteRepository
) :
    ViewModel() {
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
    val loading = MutableLiveData(false)

    fun login(username: String, password: String) {
        if (!isUserNameValid(username) || !isPasswordValid(password)) {
            usernameDataChanged(username)
            passwordDataChanged(password)
            return
        }
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            loading.value = true
            val loginResult = loginRepository.login(username, password)

            if (loginResult is Result.Success) {
                when (loginResult.data.action) {
                    LoginAction.None.value -> _loginResult.value = LoginResult(success = loginResult.data.user)
                    LoginAction.SmsVerify.value -> _loginResult.value =
                        LoginResult(navigateSmsVerify = true)
                    LoginAction.UpdateAccount.value -> _loginResult.value =
                        LoginResult(navigateUpdateAccount = true)
                }
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
            loading.value = false
        }
    }

    fun loginWithBio(username: String, signature: Signature) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            loading.value = true
            val bioChallengeResult = remoteRepository.getBioChallenge(username)
            if (bioChallengeResult is Result.Success) {
                val challenge = bioChallengeResult.data.challenge
                val salt = bioChallengeResult.data.salt
                val nonce = bioChallengeResult.data.nonce

                val stringToSign = "$challenge$salt$nonce"
                signature.update(stringToSign.toByteArray())
                val signatureBytes = signature.sign()
                val signedBySignature =
                    Base64.encodeToString(signatureBytes, Base64.URL_SAFE or Base64.NO_WRAP)

                val loginResult = loginRepository.loginWithBiometric(username, signedBySignature)

                if (loginResult is Result.Success) {
                    val userResult = loginRepository.getUser()
                    if (userResult is Result.Success) {
                        _loginResult.value =
                            LoginResult(success = userResult.data)
                    } else {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
                loading.value = false
                return@launch
            }

            loading.value = false
        }
    }

    fun handleAuthenticationError(
        errorCode: Int,
        errString: CharSequence
    ) {
        println("handleAuthenticationError... errorCode=$errorCode errString=$errString")
//        when (errorCode) {
//            BiometricPrompt.BIOMETRIC_ERROR_CANCELED -> _loginResult.value =
//                LoginResult(errorText = errString.toString())
//        }
    }

    fun clearLoginResult() {
        _loginResult.value = LoginResult()
    }

    fun checkUsername(username: String): Boolean {
        if (!isUserNameValid(username)) {
            usernameDataChanged(username)
            return false
        }
        return true
    }

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