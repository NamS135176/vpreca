package com.lifecard.vpreca.ui.card_usage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardUsageHistoryState(
    val data: List<CardUsageHistory>? = null,
    val errorText: String? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null,
)

@HiltViewModel
class CardUsageViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {
    private val _cardUsageHistoryResult = MutableLiveData<CardUsageHistoryState>()
    val cardUsageHistoryResult: LiveData<CardUsageHistoryState> = _cardUsageHistoryResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getCardUsageHistory(creditCard: CreditCard) {
        viewModelScope.launch {
            _loading.value = true
            val result = remoteRepository.getCardUsageHistory(creditCard)
            if (result is Result.Success) {
                _cardUsageHistoryResult.value = CardUsageHistoryState(data = result.data)
            } else if (result is Result.Error) {
                handleResultErrorException(result.exception)
            }
            _loading.value = false
        }
    }

    fun getCardUsageHistoryWithoutMember(creditCard: CreditCard) {
        viewModelScope.launch {
            _loading.value = true
            val result = remoteRepository.getCardUsageHistoryWithoutMember(creditCard)
            if (result is Result.Success) {
                _cardUsageHistoryResult.value = CardUsageHistoryState(data = result.data)
            } else if (result is Result.Error) {
                handleResultErrorException(result.exception)
            }
            _loading.value = false
        }
    }

    private fun handleResultErrorException(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> _cardUsageHistoryResult.value =
                CardUsageHistoryState(networkTrouble = true)
            is InternalServerException -> _cardUsageHistoryResult.value =
                CardUsageHistoryState(internalError = "")
            is ApiException -> {
                _cardUsageHistoryResult.value =
                    CardUsageHistoryState(errorText = exception.errorMessage)
            }
            else -> _cardUsageHistoryResult.value =
                CardUsageHistoryState(errorText = exception.localizedMessage)
        }
    }
}