package com.lifecard.vpreca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.UserRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class HomeViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                creditCardRepository = CreditCardRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}