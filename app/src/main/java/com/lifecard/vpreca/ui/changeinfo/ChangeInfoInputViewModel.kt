package com.lifecard.vpreca.ui.changeinfo

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R

class ChangeInfoInputViewModel : ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val idError = MutableLiveData<Int?>()
    val dateError = MutableLiveData<Int?>()
    val phoneError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val cityError = MutableLiveData<Int?>()
    val genderError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()

    val validForm = MediatorLiveData<ChangeInfoInputState>().apply {
        value = ChangeInfoInputState()
        addSource(idError) { value ->
            val previous = this.value
            this.value = previous?.copy(idError = value)
        }
        addSource(usernameError) { value ->
            val previous = this.value
            this.value = previous?.copy(usernameError = value)
        }
        addSource(dateError) { value ->
            val previous = this.value
            this.value = previous?.copy(dateError = value)
        }
        addSource(phoneError) { value ->
            val previous = this.value
            this.value = previous?.copy(phoneError = value)
        }
        addSource(genderError) { value ->
            val previous = this.value
            this.value = previous?.copy(genderError = value)
        }
        addSource(cityError) { value ->
            val previous = this.value
            this.value = previous?.copy(cityError = value)
        }
        addSource(questionError) { value ->
            val previous = this.value
            this.value = previous?.copy(questionError = value)
        }
        addSource(answerError) { value ->
            val previous = this.value
            this.value = previous?.copy(answerError = value)
        }

    }

    fun usernameDataChanged(text: String) {
        if (!isUserNameValid(text)) {
            usernameError.value = R.string.invalid_username
        } else {
            usernameError.value = null
        }
    }

    fun idDataChanged(text: String) {
        if (!isIdValid(text)) {
            idError.value = R.string.invalid_password
        } else {
            idError.value = null
        }
    }
    fun dateDataChanged(text: String) {
        if (!isDateValid(text)) {
            dateError.value = R.string.forgot_pass_error_dob
        } else {
            dateError.value = null
        }
    }

    fun phoneDataChanged(text: String) {
        if (!isPhoneValid(text)) {
            phoneError.value = R.string.forgot_pass_error_phone
        } else {
            phoneError.value = null
        }
    }

    fun questionDataChanged(text: String) {
        if (!isQuestionValid(text)) {
            questionError.value = R.string.forgot_pass_error_secret_question
        }
        else{
            questionError.value = null
        }
    }

    fun cityDataChanged(text: String) {
        if (!isQuestionValid(text)) {
            cityError.value = R.string.forgot_pass_error_secret_question
        }
        else{
            cityError.value = null
        }
    }

    fun genderDataChanged(text: String) {
        if (!isQuestionValid(text)) {
            genderError.value = R.string.forgot_pass_error_secret_question
        }
        else{
            genderError.value = null
        }
    }

    fun answerDataChanged(text: String) {
        if (!isAnswerValid(text)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
        }
        else{
            answerError.value = null
        }
    }

    private fun isPhoneValid(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun isDateValid(date: String): Boolean {

        return date != "1980年1月1日"
    }

    private fun isQuestionValid(question: String): Boolean {

        return true
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.length in 0..20 && isHalfWidth(answer)
    }

    fun isHalfWidth(text:String):Boolean{
        val regex = "[０-９ぁ-んァ-ン一-龥]".toRegex()
        return regex.find(text) == null
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length in 2..18
    }

    // A placeholder password validation check
    private fun isIdValid(id: String): Boolean {
        return id.length in 6..10
    }

}