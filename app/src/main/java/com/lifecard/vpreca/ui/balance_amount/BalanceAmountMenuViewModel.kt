package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.SuspendDealRepository
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceAmountMenuViewModel @Inject constructor(
    private val suspendDealRepository: SuspendDealRepository
) : ViewModel() {


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _suspendDealResult = MutableLiveData<SuspendDealResult>()
    val suspendDealResult: LiveData<SuspendDealResult> = _suspendDealResult
    init {
        viewModelScope.launch {
            _loading.value = true
            val result = suspendDealRepository.getListSuspendDeal()

            if (result is Result.Success) {
                _suspendDealResult.value = SuspendDealResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _suspendDealResult.value =
                        SuspendDealResult(networkTrouble = true)
                    is ApiException -> _suspendDealResult.value = SuspendDealResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage
                        )
                    )
                    is InternalServerException -> _suspendDealResult.value =
                        SuspendDealResult(internalError = "")

                    else -> _suspendDealResult.value =
                        SuspendDealResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}