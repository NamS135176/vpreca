package com.lifecard.vpreca.ui.changepass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputResultState
import com.lifecard.vpreca.utils.RegexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePassViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val oldPassError = MutableLiveData<Int?>()
    val newPassError = MutableLiveData<Int?>()
    val cfNewPassError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(ChangePassState())
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()

    private val _changePassState = MutableLiveData<ChangePassRequestState>()
    val changePassState: LiveData<ChangePassRequestState> = _changePassState
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    private fun checkOldPassValid(): Boolean {
        return if (!RegexUtils.isPasswordValid(formState.value?.oldPass)) {
            oldPassError.value = R.string.rgx_error_password
            false
        } else {
            oldPassError.value = null
            true
        }
    }

    fun oldPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(oldPass = text)
    }

    private fun checkNewPassValid(): Boolean {
        return if (!RegexUtils.isPasswordValid(formState.value?.newPass)) {
            newPassError.value = R.string.rgx_error_password
            false
        } else {
            newPassError.value = null
            true
        }
    }

    fun newPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(newPass = text)
    }

    private fun checkCfNewPassValid(): Boolean {
        return if (!isCfPasswordValid(formState.value?.cfNewPass, formState.value?.newPass)) {
            cfNewPassError.value = R.string.rgx_error_confirm_password
            false
        } else {
            cfNewPassError.value = null
            true
        }
    }

    fun cfNewPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(cfNewPass = text)
    }

    private fun isCfPasswordValid(cfPassword: String?, newPass: String?): Boolean {
//        return RegexUtils.isPasswordValid(cfPassword) && cfPassword == newPass
        return cfPassword == newPass//only compare
    }

    fun submit() {
        val isOldPassValid = checkOldPassValid()
        val isNewPassValid = checkNewPassValid()
        val isCfNewPassValid = checkCfNewPassValid()
        if(isNewPassValid && isOldPassValid && isCfNewPassValid){
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkFormValid(): Boolean {
        return formState.value?.let { form ->
            val isValid = !arrayOf(form.oldPass, form.newPass, form.cfNewPass)
                .any { it.isNullOrEmpty() }
            validForm.value = isValid
            return isValid
        } ?: false
    }

    fun changePassData(oldPass:String, newPass:String) {
        viewModelScope.launch {
            _loading.value = true
            val res = userRepository.updatePassword(oldPass, newPass)
            if (res is Result.Success) {
                _changePassState.value = ChangePassRequestState(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _changePassState.value =
                        ChangePassRequestState(networkTrouble = true)
                    is ApiException -> _changePassState.value = ChangePassRequestState(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _changePassState.value =
                        ChangePassRequestState(internalError = "")
                    else -> _changePassState.value =
                        ChangePassRequestState(
                            error = ErrorMessageException(
                                messageResId = R.string.login_failed,
                                exception = res.exception
                            )
                        )
                }
            }
            _loading.value = false
        }
    }
}