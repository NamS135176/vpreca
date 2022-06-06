package com.lifecard.vpreca.ui.forgotpass

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.ui.login.LoginFormState
import com.lifecard.vpreca.utils.RegexUtils
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ForgotPassViewModel : ViewModel() {
    val emailError = MutableLiveData<Int?>()
    val dateError = MutableLiveData<Int?>()
    val phoneError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val forgotPassState = MutableLiveData<ForgotPassState>()

    private fun checkEmailValid(text: String?): Boolean {
        return if (!RegexUtils.isEmailValid(text)) {
            emailError.value = R.string.forgot_pass_error_email
            false
        } else {
            emailError.value = null
            true
        }
    }

    private fun checkDateValid(text: String?): Boolean {
        return if (!isDateValid(text)) {
            dateError.value = R.string.forgot_pass_error_dob
            false
        } else {
            dateError.value = null
            true
        }
    }

    private fun checkPhoneValid(text: String?): Boolean {
        return if (!RegexUtils.isPhoneNumberValid(text)) {
            phoneError.value = R.string.forgot_pass_error_phone
            false
        } else {
            phoneError.value = null
            true
        }
    }

    private fun checkQuestionValid(text: String?): Boolean {
        return if (!isQuestionValid(text)) {
            questionError.value = R.string.forgot_pass_error_secret_question
            false
        } else {
            questionError.value = null
            true
        }
    }

    private fun checkAnswerValid(text: String?): Boolean {
        return if (!isAnswerValid(text)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
            false
        } else {
            answerError.value = null
            true
        }
    }

    private fun isDateValid(date: String?, pattern: String? = "yyyy年MM月dd日"): Boolean {
        return try {
            date?.let { SimpleDateFormat(pattern, Locale.JAPAN).parse(date).before(Date()) }
                ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun isQuestionValid(question: String?): Boolean {
        return !question.isNullOrEmpty()
    }

    private fun isAnswerValid(answer: String?): Boolean {
        return !answer.isNullOrEmpty() && answer.length in 0..20 && isHalfWidth(answer)
    }

    fun isHalfWidth(text: String): Boolean {
        val regex = "[０-９ぁ-んァ-ン一-龥]".toRegex()
        return regex.find(text) == null
    }

    fun submit(
        email: String?,
        date: String?,
        phone: String?,
        question: String?,
        answer: String?
    ) {
        val isValidEmail = checkEmailValid(email)
        val isValidDate = checkDateValid(date)
        val isValidPhone = checkPhoneValid(phone)
        val isValidQuestion = checkQuestionValid(question)
        val isValidAnswer = checkAnswerValid(answer)
        if (isValidEmail && isValidDate && isValidPhone && isValidQuestion && isValidAnswer) {
            forgotPassState.value = ForgotPassState(success = true)
        }
    }

    fun checkValidForm(
        email: String?,
        date: String?,
        phone: String?,
        question: String?,
        answer: String?
    ): Boolean {
        val isValid = !arrayOf(email, date, phone, question, answer).any { it.isNullOrEmpty() }

        validForm.value = isValid
        return isValid
    }

}