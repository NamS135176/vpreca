package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.ChangeInfoMemberData
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.listvpreca.CardInfoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeInfoConfirmDataViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _changeInfoState = MutableLiveData<ChangeInfoState>()
    val changeInfoState: LiveData<ChangeInfoState> = _changeInfoState
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun changeInfoData(memberInfo: ChangeInfoMemberData) {
        viewModelScope.launch {
            _loading.value = true
            val res = userRepository.changeInfoMember(memberInfo)
            if (res is Result.Success) {
                _changeInfoState.value = ChangeInfoState(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _changeInfoState.value =
                        ChangeInfoState(networkTrouble = true)
                    is ApiException -> ChangeInfoState(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _changeInfoState.value =
                            //TODO this internalError should be html from server, it will be implement later
                        ChangeInfoState(internalError = "")
                    else -> _changeInfoState.value =
                        ChangeInfoState(
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