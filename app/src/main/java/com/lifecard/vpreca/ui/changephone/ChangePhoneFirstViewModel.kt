package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneFirstViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _changeInfoDataState = MutableLiveData<ChangeInfoDataState>()
    var changeInfoDataState: LiveData<ChangeInfoDataState> = _changeInfoDataState

    fun getUser() {
        viewModelScope.launch {
            val result = userRepository.getUser()
            if (result is Result.Success) {
                _changeInfoDataState.value = ChangeInfoDataState(success = result.data)
            } else if (result is Result.Error) {
                handleResultErrorException(result.exception)
            }
        }
    }

    private fun handleResultErrorException(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> _changeInfoDataState.value =
                ChangeInfoDataState(networkTrouble = true)
            is InternalServerException -> _changeInfoDataState.value =
                    //TODO this internalError should be html from server, it will be implement later
                ChangeInfoDataState(internalError = "")
            is ApiException -> _changeInfoDataState.value = ChangeInfoDataState(
                error = ErrorMessageException(
                    errorMessage = exception.errorMessage,
                    exception = exception
                )
            )

            else -> _changeInfoDataState.value =
                ChangeInfoDataState(
                    error = ErrorMessageException(
                        errorMessage = exception.message,
                        exception = exception
                    )
                )
        }
    }

}