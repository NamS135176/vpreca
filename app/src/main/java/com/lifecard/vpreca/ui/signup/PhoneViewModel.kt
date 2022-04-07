package com.lifecard.vpreca.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhoneViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Policy Fragment"
    }
    val text: LiveData<String> = _text
    var enableNext:Boolean = false
    fun checkPhone(phone:String) {
       enableNext = isPhoneValid(phone)
    }

    private fun isPhoneValid(phone: String): Boolean {
        return if (phone.isNotBlank()) {
            Patterns.PHONE.matcher(phone).matches()
        } else {
           false
        }
    }
}