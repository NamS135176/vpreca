package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.home.CreditCardResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IssueCardByCodeSelectSourceViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {
    private val _creditCardResult = MutableLiveData<CreditCardResult>()
    val creditCardResult: LiveData<CreditCardResult> = _creditCardResult
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
                        CreditCardResult(error = ErrorMessageException(R.string.error_no_internet_connection))
                    else -> _creditCardResult.value =
                        CreditCardResult(error = ErrorMessageException(R.string.get_list_card_failure))
                }
            }
            _loading.value = false
        }
    }
}