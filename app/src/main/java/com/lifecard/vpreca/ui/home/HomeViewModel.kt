package com.lifecard.vpreca.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.SuspendDealRepository
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.balance_amount.SuspendDealResult
import com.lifecard.vpreca.ui.listvpreca.CardInfoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val suspendDealRepository: SuspendDealRepository
) : ViewModel() {
    private val _creditCardResult = MutableLiveData<CreditCardResult>()
    val creditCardResult: LiveData<CreditCardResult> = _creditCardResult
    private val _cardInfoResult = MutableLiveData<CardInfoResult?>()
    val cardInfoResult: LiveData<CardInfoResult?> = _cardInfoResult
    private val _suspendDealResult = MutableLiveData<SuspendDealResult>()
    val suspendDealResult: LiveData<SuspendDealResult> = _suspendDealResult
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        if (creditCardRepository.latestCardEmpty()) {
            loadCard(true)
        } else {
            loadCard(false)
            loadCard(true)
        }
        getListSuspend()
    }

    fun clearCardInfoResult() {
        _cardInfoResult.value = null
    }

    fun loadCard(refresh: Boolean = true) {
        viewModelScope.launch {
            _loading.value = true
            val result = creditCardRepository.getLatestCards(refresh)
            if (result is Result.Success) {
                _creditCardResult.value = CreditCardResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _creditCardResult.value =
                        CreditCardResult(networkTrouble = true)
                    is ApiException -> _creditCardResult.value = CreditCardResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage
                        )
                    )
                    else -> _creditCardResult.value =
                        CreditCardResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
                delay(200)
                _creditCardResult.value =
                    CreditCardResult()
            }
            _loading.value = false
        }
    }

    fun loadCardIfEmptyData() {
        if (creditCardRepository.latestCardEmpty() && _loading.value == false) {
            loadCard(true)
        }
    }

    fun updateCard(creditCard: CreditCard, position: Int) {
        viewModelScope.launch {
            //need implement later
            try {
                val result = _creditCardResult.value
                result?.success?.let {
                    val newList = ArrayList(it)
                    newList[position] = creditCard
                    _creditCardResult.value = CreditCardResult(success = newList)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateList(creditCard: CreditCard, position: Int) {
        viewModelScope.launch {
            //need implement later
            try {
                val result = _creditCardResult.value
                result?.success?.let {
                    val newList = ArrayList(it)
                    newList[position] = creditCard
                    _creditCardResult.value = CreditCardResult(success = newList)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun creditCardSelectDataChanged(creditCard: CreditCard) {
        viewModelScope.launch {
            _loading.value = true
            val res = creditCardRepository.getCard(
                creditCard.cardSchemeId,
                creditCard.precaNumber,
                creditCard.vcn
            )
            if (res is Result.Success) {
                _cardInfoResult.value = CardInfoResult(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _cardInfoResult.value =
                        CardInfoResult(networkTrouble = true)
                    is ApiException -> _cardInfoResult.value = CardInfoResult(
                        error = ErrorMessageException(
                            errorMessage = res.exception.message
                        )
                    )
                    else -> _cardInfoResult.value =
                        CardInfoResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
                delay(200)
                _cardInfoResult.value =
                    CardInfoResult()
            }
            _loading.value = false
        }
    }

    fun getListSuspend() {
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
                            errorMessage = result.exception.message
                        )
                    )
                    else -> _suspendDealResult.value =
                        SuspendDealResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
                delay(200)
                _suspendDealResult.value = SuspendDealResult()
            }
            _loading.value = false
        }
    }

}