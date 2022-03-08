package com.lifecard.vpreca.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import kotlinx.coroutines.launch
import com.lifecard.vpreca.data.Result

class HomeViewModel(private val creditCardRepository: CreditCardRepository) : ViewModel() {
    private val _creditCardResult = MutableLiveData<CreditCardResult>()
    val creditCardResult: LiveData<CreditCardResult> = _creditCardResult

    init {
        viewModelScope.launch {
            val result = creditCardRepository.getLatestCards(true)
            if (result is Result.Success) {
                _creditCardResult.value = CreditCardResult(success = result.data)
            } else {
                _creditCardResult.value = CreditCardResult(error = R.string.get_list_card_failure)
            }
        }
    }
}