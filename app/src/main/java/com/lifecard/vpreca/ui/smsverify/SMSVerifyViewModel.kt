package com.lifecard.vpreca.ui.smsverify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.DeviceID
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.ui.login.LoginResult
import com.lifecard.vpreca.ui.signup.ConfirmPhoneState
import com.lifecard.vpreca.utils.RequestHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SMSVerifyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepository: UserRepository,
    private val deviceID: DeviceID,
) : ViewModel() {
    val cfPhoneError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ConfirmPhoneState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _sendSMSRequestResult = MutableLiveData<SendSMSRequestState>()
    val sendSMSRequestResult: LiveData<SendSMSRequestState> = _sendSMSRequestResult

    private val _sendSMSConfirmResult = MutableLiveData<SendSMSConfirmState>()
    val sendSMSConfirmResult: LiveData<SendSMSConfirmState> = _sendSMSConfirmResult

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun sendSMSRequest(
        loginId: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = userRepository.sendSMSRequest(
                loginId
            )

            if (result is Result.Success) {
                _sendSMSRequestResult.value = SendSMSRequestState(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _sendSMSRequestResult.value =
                        SendSMSRequestState(networkTrouble = true)
                    is ApiException -> _sendSMSRequestResult.value = SendSMSRequestState(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage,
                            exception = result.exception
                        )
                    )
                    is InternalServerException -> _sendSMSRequestResult.value =
                        SendSMSRequestState(internalError = "")
                    else -> _sendSMSRequestResult.value =
                        SendSMSRequestState(
                            error = ErrorMessageException(
                                messageResId = R.string.login_failed,
                                exception = result.exception
                            )
                        )
                }
            }
            _loading.value = false
        }
    }

    fun sendSMSConfirm(
        certType: String,
        loginId: String,
        certCode: String,
        extCertDealId: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = userRepository.sendSMSIvrConfirm(
                certType = certType,
                loginId = loginId,
                certCode = certCode,
                extCertDealId = extCertDealId
            )

            if (result is Result.Success) {
                _sendSMSConfirmResult.value = SendSMSConfirmState(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _sendSMSConfirmResult.value =
                        SendSMSConfirmState(networkTrouble = true)
                    is ApiException -> {
                        when(result.exception.resultCode){
                            "2602105" -> {
                                _sendSMSConfirmResult.value = SendSMSConfirmState(isExpire = true)
                            }
                            "2602107" -> {
                                _sendSMSConfirmResult.value = SendSMSConfirmState(isOver = true)
                            }
                            else -> {
                                _sendSMSConfirmResult.value = SendSMSConfirmState(
                                    error = ErrorMessageException(
                                        errorMessage = result.exception.errorMessage,
                                        exception = result.exception
                                    )
                                )
                            }
                        }
                    }
                    is InternalServerException -> _sendSMSConfirmResult.value =
                        SendSMSConfirmState(internalError = "")
                    else -> _sendSMSConfirmResult.value =
                        SendSMSConfirmState(
                            error = ErrorMessageException(
                                messageResId = R.string.login_failed,
                                exception = result.exception
                            )
                        )
                }
            }
            _loading.value = false
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
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
            _loading.value = false
        }
    }

    private fun handleResultErrorException(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> _loginResult.value =
                LoginResult(networkTrouble = true)
            is InternalServerException -> _loginResult.value =
                LoginResult(internalError = "")
            is ApiException -> {
                _loginResult.value = LoginResult(
                    error = ErrorMessageException(
                        errorMessage = exception.errorMessage,
                        exception = exception,
                    )
                )
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
        if (isCfPhoneValid) {
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