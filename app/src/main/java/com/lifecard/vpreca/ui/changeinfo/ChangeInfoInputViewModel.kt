package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.utils.RegexUtils

data class ChangeInfoInputResultState(
    val success: Boolean? = null
)

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
    val kanaFullNameError = MutableLiveData<Int?>()
    val hiraFullNameError = MutableLiveData<Int?>()
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()
    val formState = MutableLiveData(ChangeInfoInputState())

    val validForm = MutableLiveData<Boolean>()

    private fun checkNicknameValid(): Boolean {
        return if (!RegexUtils.isNicknameValid(formState.value?.nickname)) {
            nicknameError.value = R.string.rgx_error_nickname
            false
        } else {
            nicknameError.value = null
            true
        }
    }

    fun nicknameDataChanged(text: String) {
        formState.value = formState.value?.copy(nickname = text)
    }


    private fun checkLoginIdValid(): Boolean {
        return if (!RegexUtils.isLoginIdValid(formState.value?.loginId)) {
            idError.value = R.string.invalid_username
            false
        } else {
            idError.value = null
            true
        }
    }

    fun loginIdDataChanged(text: String) {
        formState.value = formState.value?.copy(loginId = text)
    }

    private fun checkQuestionValid(): Boolean {
        return if (!isQuestionValid(formState.value?.question)) {
            questionError.value = R.string.forgot_pass_error_secret_question
            false
        } else {
            questionError.value = null
            true
        }
    }

    fun questionDataChanged(text: String) {
        formState.value = formState.value?.copy(question = text)
    }

    private fun checkCityValid(): Boolean {
        return if (!isCityValid(formState.value?.city)) {
            cityError.value = R.string.forgot_pass_error_secret_question
            false
        } else {
            cityError.value = null
            true
        }
    }

    fun cityDataChanged(text: String) {
        formState.value = formState.value?.copy(city = text)
    }

    private fun checkEmail1Valid(): Boolean {
        return if (!RegexUtils.isEmailValid(formState.value?.email1)) {
            email1Error.value = R.string.rgx_error_email
            false
        } else {
            email1Error.value = null
            true
        }
    }

    fun email1DataChanged(text: String) {
        formState.value = formState.value?.copy(email1 = text)
    }

    private fun checkEmail2Valid(): Boolean {
        val email2 = formState.value?.email2
        return if (email2.isNullOrEmpty()) {
            true
        } else if (!RegexUtils.isEmailValid(email2) || email2.contentEquals(
                formState.value?.email1,
                true
            )
        ) {
            email2Error.value = R.string.rgx_error_email
            false
        } else {
            email2Error.value = null
            true
        }
    }

    fun email2DataChanged(text: String) {
        formState.value = formState.value?.copy(email2 = text)
    }

    private fun checkEmail1ConfirmValid(): Boolean {
        return if (!isEmail1ConfirmValid(formState.value?.email1Confirm, formState.value?.email1)) {
            email1ConfirmError.value = R.string.rgx_error_email
            false
        } else {
            email1ConfirmError.value = null
            true
        }
    }

    fun email1ConfirmDataChanged(text: String) {
        formState.value = formState.value?.copy(email1Confirm = text)
    }

    private fun checkEmail2ConfirmValid(): Boolean {
        return if (!isEmail2ConfirmValid(formState.value?.email2Confirm, formState.value?.email2)) {
            email2ConfirmError.value = R.string.rgx_error_email
            false
        } else {
            email2ConfirmError.value = null
            true
        }
    }

    fun email2ConfirmDataChanged(text: String) {
        formState.value = formState.value?.copy(email2Confirm = text)
    }

    private fun checkAnswerValid(): Boolean {
        return if (!isAnswerValid(formState.value?.answer)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
            true
        } else {
            answerError.value = null
            false
        }
    }

    fun answerDataChanged(text: String) {
        formState.value = formState.value?.copy(answer = text)
    }

    private fun checkKanaNameValid(): Boolean {
        val kanaFirstName = formState.value?.kanaFirstName
        val kanaLastName = formState.value?.kanaLastName
        return if (kanaFirstName.isNullOrEmpty() || kanaLastName.isNullOrEmpty()
            || !RegexUtils.isKatakanaFullWidth(kanaFirstName)
            || !RegexUtils.isKatakanaFullWidth(kanaLastName)
            || kanaFirstName.length.plus(kanaLastName.length) !in 0..19
        ) {
            kanaFullNameError.value = R.string.rgx_error_kana_name
            true
        } else {
            kanaFullNameError.value = null
            false
        }
    }

    private fun checkHiraNameValid(): Boolean {
        val hiraFirstName = formState.value?.hiraFirstName
        val hiraLastName = formState.value?.hiraLastName
        return if (hiraFirstName.isNullOrEmpty() || hiraLastName.isNullOrEmpty()
            || !RegexUtils.isSecondName(hiraFirstName)
            || !RegexUtils.isSecondName(hiraLastName)
            || hiraFirstName.length.plus(hiraLastName.length) !in 0..19
        ) {
            hiraFullNameError.value = R.string.rgx_error_hira_name
            true
        } else {
            hiraFullNameError.value = null
            false
        }
    }

    fun kanaFirstNameDataChanged(text: String) {
        formState.value = formState.value?.copy(kanaFirstName = text)
    }

    fun kanaLastNameDataChanged(text: String) {
        formState.value = formState.value?.copy(kanaLastName = text)
    }

    fun hiraFirstNameDataChanged(text: String) {
        formState.value = formState.value?.copy(hiraFirstName = text)
    }

    fun hiraLastNameDataChanged(text: String) {
        formState.value = formState.value?.copy(hiraLastName = text)
    }

    private fun isQuestionValid(question: String?): Boolean {
        return !question.isNullOrEmpty()
    }

    private fun isCityValid(city: String?): Boolean {
        return !city.isNullOrEmpty()
    }

    private fun isAnswerValid(answer: String?): Boolean {
        return !answer.isNullOrEmpty() && answer.length in 0..20 && isHalfWidth(answer)
    }

    fun isHalfWidth(text: String): Boolean {
        val regex = "[０-９ぁ-んァ-ン一-龥]".toRegex()
        return regex.find(text) == null
    }

    private fun isEmail1ConfirmValid(cfEmail: String?, email: String?): Boolean {
        return RegexUtils.isEmailValid(cfEmail) && email == cfEmail
    }

    private fun isEmail2ConfirmValid(cfEmail: String?, email: String?): Boolean {
        return RegexUtils.isEmailValid(email) && email == cfEmail
    }

    fun submit() {
        val isValidEmail1 = checkEmail1Valid()
        val isValidEmail1Confirm = checkEmail1ConfirmValid()
        val isValidEmail2 = checkEmail2Valid()
        val isValidEmail2Confirm = checkEmail2ConfirmValid()
        val isValidNickname = checkNicknameValid()
        val isValidLoginId = checkLoginIdValid()
        val isValidQuestion = checkQuestionValid()
        val isValidCity = checkCityValid()
        val isValidAnswer = checkAnswerValid()
        val isValidKanaName = checkKanaNameValid()
        val isValidHiraNam = checkHiraNameValid()
        if (isValidEmail1 && isValidEmail1Confirm && isValidEmail2 && isValidEmail2Confirm && isValidNickname && isValidLoginId && isValidQuestion && isValidCity && isValidAnswer
            && isValidKanaName && isValidHiraNam
        ) {
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkValidForm(): Boolean {
        val isValid = formState.value?.let { form ->
            val kataName = "${form.kanaFirstName ?: ""}${form.kanaLastName ?: ""}"
            val hiraName = "${form.hiraFirstName ?: ""}${form.hiraLastName ?: ""}"
            val fields = arrayOf(
                form.nickname,
                form.loginId,
                form.question,
                form.city,
                form.answer,
                form.email1,
                form.email1Confirm,
                kataName,
                hiraName,
            )
            !fields.any { it.isNullOrEmpty() }
        } ?: false
        validForm.value = isValid
        return isValid
    }

}