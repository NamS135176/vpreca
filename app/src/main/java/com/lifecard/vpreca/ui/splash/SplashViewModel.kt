package com.lifecard.vpreca.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.home.CreditCardResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashResult(
    val user: MemberInfo? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userManager: UserManager
) :
    ViewModel() {
    var splashResult = MutableLiveData<SplashResult>()

    fun getUser() {
        viewModelScope.launch {
            val result = userRepository.getUser(userManager.loginId, userManager.memberNumber)
            if (result is Result.Success) {
                splashResult.value = SplashResult(user = result.data)
            } else if (result is Result.Error) {
                when (result.exception) {
                    is NoConnectivityException -> splashResult.value =
                        SplashResult(networkTrouble = true)
                    else -> splashResult.value =
                        SplashResult(
                            error = ErrorMessageException(
                                R.string.get_user_failure,
                                result.exception
                            )
                        )
                }
            }
        }
    }
}