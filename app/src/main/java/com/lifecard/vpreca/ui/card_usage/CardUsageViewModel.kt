package com.lifecard.vpreca.ui.card_usage

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CardUsageHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardUsageViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {
    private val _cardUsageHistoryResult = MutableLiveData<Result<List<CardUsageHistory>>>()
    val cardUsageHistoryResult: LiveData<Result<List<CardUsageHistory>>> = _cardUsageHistoryResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getCardUsageHistory() {
        viewModelScope.launch {
            _loading.value = true
            var result = remoteRepository.getCardUsageHistory()
            _cardUsageHistoryResult.value = result
            _loading.value = false
        }
    }
}