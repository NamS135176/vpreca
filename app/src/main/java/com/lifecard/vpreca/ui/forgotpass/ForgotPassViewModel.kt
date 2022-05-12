package com.lifecard.vpreca.ui.forgotpass

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.login.LoginFormState

class ForgotPassViewModel : ViewModel() {
    val emailError = MutableLiveData<Int?>()
    val dateError = MutableLiveData<Int?>()
    val phoneError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()
    val validForm = MediatorLiveData<ForgotPassState>().apply {
        value = ForgotPassState()
        addSource(emailError) { value ->
            val previous = this.value
            this.value = previous?.copy(emailError = value)
        }
        addSource(dateError) { value ->
            val previous = this.value
            this.value = previous?.copy(dateError = value)
        }
        addSource(phoneError) { value ->
            val previous = this.value
            this.value = previous?.copy(phoneError = value)
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

    fun emailDataChanged(text: String) {
        if (!isEmailValid(text)) {
            emailError.value = R.string.forgot_pass_error_email
        } else {
            emailError.value = null
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

    fun answerDataChanged(text: String) {
        if (!isAnswerValid(text)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
        }
        else{
            answerError.value = null
        }
    }
    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneValid(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun isDateValid(date: String): Boolean {

        return date != "年/月/日"
    }

    private fun isQuestionValid(question: String): Boolean {

        return true
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.length in 0..20
    }
}