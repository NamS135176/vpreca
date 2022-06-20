package com.lifecard.vpreca.ui.web_direct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.RemoteRepository
import com.lifecard.vpreca.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebDirectViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    val loading = MutableLiveData<Boolean>(false)
    val otp = MutableLiveData<String?>()

    fun getOpt() {
        viewModelScope.launch {
            loading.value = true
            val result = remoteRepository.requestWebDirectOtp()
            if (result is Result.Success) {
                otp.value = result.data.otp
            }
            loading.value = false
        }
    }

}