package com.lifecard.vpreca.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashState(
    val user: MemberInfo? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userManager: UserManager
) :
    ViewModel() {
    var splashState = MutableLiveData<SplashState>()

    fun getUser() {
        viewModelScope.launch {
            val result = userRepository.getUser(userManager.loginId, userManager.memberNumber)
            if (result is Result.Success) {
                splashState.value = SplashState(user = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> splashState.value =
                        SplashState(networkTrouble = true)
                    else -> splashState.value =
                        SplashState(
                            error = ErrorMessageException(
                                messageResId = R.string.get_user_failure,
                                exception = result.exception
                            )
                        )
                }
            }
        }
    }
}