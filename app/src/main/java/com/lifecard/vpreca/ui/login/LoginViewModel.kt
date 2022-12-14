package com.lifecard.vpreca.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.DeviceID
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.BiometricHelper
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.RequestHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: UserRepository,
    private val secureStore: SecureStore,
    private val userManager: UserManager,
    private val deviceID: DeviceID,
) :
    ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val passwordError = MutableLiveData<Int?>()
    val validForm = MutableLiveData(false)

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    val loading = MutableLiveData(false)

    fun login(username: String, password: String) {
        val validUsername = checkUsername(username)
        val validPassword = checkPassword(password)
        if (!validUsername || !validPassword) {
            return
        }
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            loading.value = true
            val loginResult = loginRepository.login(
                RequestHelper.createLoginRequest(
                    deviceId = deviceID.deviceId,
                    loginId = username,
                    loginPassword = password
                )
            )
            if (loginResult is Result.Success) {
                _loginResult.value =
                    LoginResult(success = loginResult.data.response.memberInfo)
            } else if (loginResult is Result.Error) {
                handleResultErrorException(loginResult.exception)
            }
            loading.value = false
        }
    }


    fun loginWithCipher(cipher: Cipher) {
        viewModelScope.launch {
            loading.value = true
            secureStore.updateDecryptBioAuthTokenStore(cipher)
            secureStore.getAuthToken()?.let { authToken ->
                userManager.authToken = authToken
                val userResult = loginRepository.getUser()
                if (userResult is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = userResult.data)

                } else if (userResult is Result.Error) {
                    handleResultErrorException(userResult.exception)
                }
            }
            loading.value = false
        }
    }

    private fun handleResultErrorException(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> _loginResult.value =
                LoginResult(networkTrouble = true)
            is InternalServerException -> _loginResult.value =
                LoginResult(internalError = "")
            is ApiException -> {
                if (exception.resultCode == "1101302") {//case require sms verification
                    _loginResult.value = LoginResult(
                        smsVerification = true
                    )
                } else {
                    _loginResult.value = LoginResult(
                        error = ErrorMessageException(
                            errorMessage = exception.errorMessage,
                            exception = exception,
                        )
                    )
                }
            }
            else -> _loginResult.value =
                LoginResult(
                    error = ErrorMessageException(
                        messageResId = R.string.login_failed,
                        exception = exception
                    )
                )
        }
    }
    fun clearLoginResultError() {
        _loginResult.value = LoginResult()
    }

    fun handleAuthenticationError(
        errorCode: Int,
        errString: CharSequence
    ) {
        println("handleAuthenticationError... errorCode=$errorCode errString=$errString")
        val messageResId = BiometricHelper.getMessageIdByErrorCode(errorCode)
        messageResId?.let {
            _loginResult.value = LoginResult(
                error = ErrorMessageException(
                    messageResId = it
                )
            )
        } ?: kotlin.run {
        }
    }

    private fun checkUsername(username: String): Boolean {
        if (!RegexUtils.isLoginIdValid(username)) {
            usernameError.value = R.string.invalid_username
            return false
        }
        usernameError.value = null
        return true
    }

    private fun checkPassword(password: String): Boolean {
        if (!RegexUtils.isPasswordValid(password)) {
            passwordError.value = R.string.invalid_password
            return false
        }
        passwordError.value = null
        return true
    }

    fun checkValidForm(username: String, password: String) {
        validForm.value = username.isNotEmpty() && password.isNotEmpty()
    }
}