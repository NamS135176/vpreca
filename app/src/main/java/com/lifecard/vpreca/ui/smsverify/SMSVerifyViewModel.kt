package com.lifecard.vpreca.ui.smsverify

import androidx.lifecycle.*
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.ui.signup.ConfirmPhoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SMSVerifyViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val cfPhoneError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ConfirmPhoneState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _sendSMSRequestResult = MutableLiveData<SendSMSRequestState>()
    val sendSMSRequestResult: LiveData<SendSMSRequestState> = _sendSMSRequestResult

    fun sendSMSRequest(
        memberNumber: String,
        telephoneNumber: String,
        certType: String,
        operationType: String,
        certSumFlg: String,
        operationSumFlg: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = userRepository.sendSMSRequest(
                memberNumber,
                telephoneNumber,
                certType,
                operationType,
                certSumFlg,
                operationSumFlg
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
                            //TODO this internalError should be html from server, it will be implement later
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

    private fun checkCfPhoneValid(): Boolean {
        return if (formState.value?.confirmCode?.length != 4) {
            cfPhoneError.value = R.string.error_code
            false
        } else {
            cfPhoneError.value = null
            true
        }
//        return true
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