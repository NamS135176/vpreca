package com.lifecard.vpreca.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import kotlinx.coroutines.launch
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _creditCardResult = MutableLiveData<CreditCardResult>()
    val creditCardResult: LiveData<CreditCardResult> = _creditCardResult

    init {
        viewModelScope.launch {
            val result = creditCardRepository.getLatestCards(true)

            if (result is Result.Success) {
                _creditCardResult.value = CreditCardResult(success = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> _creditCardResult.value =
                        CreditCardResult(error = ErrorMessageException(R.string.error_no_internet_connection))
                    else -> _creditCardResult.value =
                        CreditCardResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
        }
    }
}