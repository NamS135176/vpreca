package com.lifecard.vpreca.ui.listvpreca

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.home.CreditCardResult
import com.lifecard.vpreca.ui.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListVprecaViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {
    private val _creditCardResult = MutableLiveData<CreditCardResult>()
    val creditCardResult: LiveData<CreditCardResult> = _creditCardResult

    private val _cardInfoResult = MutableLiveData<CardInfoResult>()
    val cardInfoResult: LiveData<CardInfoResult> = _cardInfoResult

    private val _creditCardSelect = MutableLiveData<CreditCard>()
    val creditCardSelect : LiveData<CreditCard> = _creditCardSelect
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    init {
        viewModelScope.launch {
            _loading.value = true
            val result = creditCardRepository.getLatestCards(true)

            if (result is Result.Success) {
                _creditCardResult.value = CreditCardResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _creditCardResult.value =
                        CreditCardResult(networkTrouble = true)
                    is ApiException -> _creditCardResult.value = CreditCardResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage,
                            exception = result.exception
                        )
                    )
                    is InternalServerException -> _creditCardResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        CreditCardResult(internalError = "")
                    else -> _creditCardResult.value =
                        CreditCardResult( error = ErrorMessageException(
                            messageResId = R.string.login_failed,
                            exception = result.exception
                        ))
                }
            }
            _loading.value = false
        }
    }

    fun getListCard(){
        viewModelScope.launch {
            _loading.value = true
            val result = creditCardRepository.getLatestCards(true)

            if (result is Result.Success) {
                _creditCardResult.value = CreditCardResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _creditCardResult.value =
                        CreditCardResult(networkTrouble = true)
                    is ApiException -> _creditCardResult.value = CreditCardResult(
                        error = ErrorMessageException(
                            errorMessage = result.exception.errorMessage,
                            exception = result.exception
                        )
                    )
                    is InternalServerException -> _creditCardResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        CreditCardResult(internalError = "")
                    else -> _creditCardResult.value =
                        CreditCardResult( error = ErrorMessageException(
                            messageResId = R.string.login_failed,
                            exception = result.exception
                        ))
                }
            }
            _loading.value = false
        }
    }

    fun changeSelect(creditCard: CreditCard){
        _creditCardSelect.value = creditCard
    }

    fun creditCardSelectDataChanged(creditCard: CreditCard) {
        viewModelScope.launch {
            _loading.value = true
            val res = creditCardRepository.getCard(creditCard.cardSchemeId, creditCard.precaNumber, creditCard.vcn)
            if (res is Result.Success) {
                _cardInfoResult.value = CardInfoResult(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _cardInfoResult.value =
                        CardInfoResult(networkTrouble = true)
                    is ApiException -> CardInfoResult(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _cardInfoResult.value =
                            //TODO this internalError should be html from server, it will be implement later
                        CardInfoResult(internalError = "")
                    else -> _cardInfoResult.value =
                        CardInfoResult( error = ErrorMessageException(
                            messageResId = R.string.login_failed,
                            exception = res.exception
                        ))
                }
            }
            _loading.value = false
        }
    }

}