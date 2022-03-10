package com.lifecard.vpreca.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()


}