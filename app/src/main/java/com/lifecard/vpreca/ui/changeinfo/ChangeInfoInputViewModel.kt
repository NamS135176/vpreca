package com.lifecard.vpreca.ui.changeinfo

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.utils.RegexUtils

class ChangeInfoInputViewModel : ViewModel() {
    val nicknameError = MutableLiveData<Int?>()
    val idError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val cityError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()
    val email1Error = MutableLiveData<Int?>()
    val email1ConfirmError = MutableLiveData<Int?>()
    val email2Error = MutableLiveData<Int?>()
    val email2ConfirmError = MutableLiveData<Int?>()


    val validForm = MediatorLiveData<ChangeInfoInputState>().apply {
        value = ChangeInfoInputState()
        addSource(idError) { value ->
            val previous = this.value
            this.value = previous?.copy(idError = value)
        }
        addSource(nicknameError) { value ->
            val previous = this.value
            this.value = previous?.copy(nicknameError = value)
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
        addSource(email1Error) { value ->
            val previous = this.value
            this.value = previous?.copy(email1Error = value)
        }
        addSource(email1ConfirmError) { value ->
            val previous = this.value
            this.value = previous?.copy(email1ConfirmError = value)
        }
        addSource(email2Error) { value ->
            val previous = this.value
            this.value = previous?.copy(email2Error = value)
        }
        addSource(email2ConfirmError) { value ->
            val previous = this.value
            this.value = previous?.copy(email2ConfirmError = value)
        }
    }

    fun nicknameDataChanged(text: String) {
        if (!RegexUtils.isNicknameValid(text)) {
            nicknameError.value = R.string.rgx_error_nickname
        } else {
            nicknameError.value = null
        }
    }

    fun idDataChanged(text: String) {
        if (!RegexUtils.isLoginIdValid(text)) {
            idError.value = R.string.invalid_username
        } else {
            idError.value = null
        }
    }

    fun questionDataChanged(text: String) {
        if (!isQuestionValid(text)) {
            questionError.value = R.string.forgot_pass_error_secret_question
        } else {
            questionError.value = null
        }
    }

    fun cityDataChanged(text: String) {
        if (!isQuestionValid(text)) {
            cityError.value = R.string.forgot_pass_error_secret_question
        } else {
            cityError.value = null
        }
    }

    fun email1DataChanged(text: String) {
        if (!isEmailValid(text)) {
            cityError.value = R.string.rgx_error_email
        } else {
            cityError.value = null
        }
    }

    fun email2DataChanged(text: String) {
        if (!isEmailValid(text)) {
            cityError.value = R.string.rgx_error_email
        } else {
            cityError.value = null
        }
    }

    fun email1ConfirmDataChanged(text: String, email: String) {
        if (!isEmail1ConfirmValid(text,email)) {
            cityError.value = R.string.rgx_error_email
        } else {
            cityError.value = null
        }
    }

    fun email2ConfirmDataChanged(text: String, email: String) {
        if (!isEmail2ConfirmValid(text, email)) {
            cityError.value = R.string.rgx_error_email
        } else {
            cityError.value = null
        }
    }

    fun answerDataChanged(text: String) {
        if (!isAnswerValid(text)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
        } else {
            answerError.value = null
        }
    }


    private fun isQuestionValid(question: String): Boolean {

        return true
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.length in 0..20 && isHalfWidth(answer)
    }

    fun isHalfWidth(text: String): Boolean {
        val regex = "[０-９ぁ-んァ-ン一-龥]".toRegex()
        return regex.find(text) == null
    }

    fun isEmailValid(email: String): Boolean {
        return RegexUtils.isEmailValid(email)
    }

    fun isEmail1ConfirmValid(cfEmail: String, email: String): Boolean {
        return RegexUtils.isEmailValid(cfEmail) && email == cfEmail
    }

    fun isEmail2ConfirmValid(cfEmail: String, email: String): Boolean {
        return RegexUtils.isEmailValid(email) && email == cfEmail
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return RegexUtils.isLoginIdValid(username)
    }

    // A placeholder password validation check
    private fun isIdValid(id: String): Boolean {
        return id.length in 6..10
    }

}