package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceAmountMenuViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {
    private val _suspendDealResult = MutableLiveData<SuspendDealResult>()
    val suspendDealResult: LiveData<SuspendDealResult> = _suspendDealResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    init {
        viewModelScope.launch {
            _loading.value = true
            val result = creditCardRepository.getListSuspendDeal()

            if (result is Result.Success) {
                _suspendDealResult.value = SuspendDealResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _suspendDealResult.value =
                        SuspendDealResult(error = ErrorMessageException(R.string.error_no_internet_connection_content))
                    else -> _suspendDealResult.value =
                        SuspendDealResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}