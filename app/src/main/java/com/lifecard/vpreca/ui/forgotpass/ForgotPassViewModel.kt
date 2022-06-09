package com.lifecard.vpreca.ui.forgotpass

import android.util.Patterns
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.InternalServerException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.changepass.ChangePassRequestState
import com.lifecard.vpreca.ui.login.LoginFormState
import com.lifecard.vpreca.utils.RegexUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val emailError = MutableLiveData<Int?>()
    val dateError = MutableLiveData<Int?>()
    val phoneError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()
    val validForm = MutableLiveData<Boolean>()
    val forgotPassState = MutableLiveData<ForgotPassState>()

    private val _resetPassState = MutableLiveData<ResetPassReqState>()
    val resetPassState: LiveData<ResetPassReqState> = _resetPassState
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

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
        return RegexUtils.isAnswerValid(answer)
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

    fun resetPassData(email:String, birthday:String, phone:String, secretQuestion:String, secretAnswer:String) {
        viewModelScope.launch {
            _loading.value = true
            val res = userRepository.resetPassword(email, birthday, phone, secretQuestion, secretAnswer)
            if (res is Result.Success) {
                _resetPassState.value = ResetPassReqState(success = res.data)
            } else if (res is Result.Error) {
                when (res.exception) {
                    is NoConnectivityException -> _resetPassState.value =
                        ResetPassReqState(networkTrouble = true)
                    is ApiException -> _resetPassState.value = ResetPassReqState(
                        error = ErrorMessageException(
                            errorMessage = res.exception.errorMessage,
                            exception = res.exception
                        )
                    )
                    is InternalServerException -> _resetPassState.value =
                            //TODO this internalError should be html from server, it will be implement later
                        ResetPassReqState(internalError = "")
                    else -> _resetPassState.value =
                        ResetPassReqState(
                            error = ErrorMessageException(
                                messageResId = R.string.login_failed,
                                exception = res.exception
                            )
                        )
                }
            }
            _loading.value = false
        }
    }

}