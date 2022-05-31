package com.lifecard.vpreca.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.MemberInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository, private val  userManager: UserManager) :
    ViewModel() {
    private val _user = MutableLiveData<MemberInfo>()
    var user: LiveData<MemberInfo> = _user
    var error = MutableLiveData<String?>()

    fun getUser() {
        viewModelScope.launch {
            val result = userRepository.getUser(userManager.loginId, userManager.memberNumber)
            if (result is Result.Success) {
                _user.value = result.data
            } else if (result is Result.Error){
                error.value = result.exception.message
            }
        }
    }
}