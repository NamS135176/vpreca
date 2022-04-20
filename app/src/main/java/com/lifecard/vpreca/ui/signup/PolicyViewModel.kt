package com.lifecard.vpreca.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PolicyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Policy Fragment"
    }
    val text: LiveData<String> = _text
}