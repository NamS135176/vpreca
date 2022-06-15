package com.lifecard.vpreca.ui.login

import android.hardware.biometrics.BiometricPrompt
import android.util.Base64
import androidx.lifecycle.*
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.api.MessageDigest
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.splash.SplashState
import com.lifecard.vpreca.utils.BiometricHelper
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.RequestHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.Signature
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: UserRepository,
    private val remoteRepository: RemoteRepository,
    private val secureStore: SecureStore,
    private val userManager: UserManager
) :
    ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val passwordError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>(false)

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

//            val messageDigest = MessageDigest().sign("{\"brandPrecaApi\":{\"head\":{\"nodeId\":\"\",\"searchKey\":\"\",\"processId\":\"\",\"brandSchemeId\":\"\",\"requestDate\":\"20220531083300\",\"centerId\":\"\",\"messageType\":\"LoginReq\"},\"request\":{\"loginInfo\":{\"loginPassword\":\"aaaaaaaa\",\"loginId\":\"aaaaaa\"}}}}")
//            println("messageDigest = $messageDigest")
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
                    //TODO this internalError should be html from server, it will be implement later
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
//            _loginResult.value =
//                LoginResult(errorText = errString.toString())
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