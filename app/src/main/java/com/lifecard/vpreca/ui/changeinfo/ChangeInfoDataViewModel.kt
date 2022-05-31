package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeInfoDataViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _user = MutableLiveData<MemberInfo>()
    var user: LiveData<MemberInfo> = _user
    var error = MutableLiveData<String?>()

    fun getUser() {
        viewModelScope.launch {
            val result = userRepository.getUser()
            if (result is Result.Success) {
                _user.value = result.data
            } else if (result is Result.Error) {
                error.value = result.exception.message
            }
        }
    }

}