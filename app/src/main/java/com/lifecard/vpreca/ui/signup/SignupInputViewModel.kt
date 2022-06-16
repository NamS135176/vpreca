package com.lifecard.vpreca.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.SignupInputState
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.RegexUtils

data class SignupInputResultState(
    val success: Boolean? = null
)

class SignupInputViewModel : ViewModel() {
    val usernameError = MutableLiveData<Int?>()
    val loginIdError = MutableLiveData<Int?>()
    val dateError = MutableLiveData<Int?>()
    val phoneError = MutableLiveData<Int?>()
    val questionError = MutableLiveData<Int?>()
    val cityError = MutableLiveData<Int?>()
    val genderError = MutableLiveData<Int?>()
    val answerError = MutableLiveData<Int?>()
    val passwordError = MutableLiveData<Int?>()
    val cfPasswordError = MutableLiveData<Int?>()
    val nameError = MutableLiveData<Array<Number?>?>()
    val kanaNameError = MutableLiveData<Boolean>()
    val furiganaNameError = MutableLiveData<Boolean>()
    val validForm = MutableLiveData<Boolean>()
    val formState = MutableLiveData(SignupInputState())
    val formResultState = MutableLiveData<SignupInputResultState>()

    private fun checkUsernameValid(): Boolean {
        return if (!RegexUtils.isNicknameValid(formState.value?.nickname)) {
            usernameError.value = R.string.invalid_username
            false
        } else {
            usernameError.value = null
            true
        }
    }

    fun usernameDataChanged(text: String) {
        formState.value = formState.value?.copy(nickname = text)
    }

    private fun checkLoginIdValid(): Boolean {
        return if (!RegexUtils.isLoginIdValid(formState.value?.loginId)) {
            loginIdError.value = R.string.rgx_error_login_id
            false
        } else {
            loginIdError.value = null
            true
        }
    }

    fun loginIdDataChanged(text: String) {
        formState.value = formState.value?.copy(loginId = text)
    }

    private fun checkDateValid(): Boolean {
        return if (!isDateValid(formState.value?.date)) {
            dateError.value = R.string.rgx_error_birthdate
            false
        } else {
            dateError.value = null
            true
        }
    }

    fun dateDataChanged(text: String) {
        formState.value = formState.value?.copy(date = text)
    }

    private fun checkPasswordValid(): Boolean {
        return if (!RegexUtils.isPasswordValid(formState.value?.password)) {
            passwordError.value = R.string.rgx_error_password
            false
        } else {
            passwordError.value = null
            true
        }
    }

    fun passwordDataChanged(text: String) {
        formState.value = formState.value?.copy(password = text)
    }

    private fun checkCfPasswordValid(): Boolean {
        return if (!isCfPasswordValid(formState.value?.password, formState.value?.cfPassword)) {
            cfPasswordError.value = R.string.rgx_error_cf_password
            false
        } else {
            cfPasswordError.value = null
            true
        }
    }

    fun cfPasswordDataChanged(text: String) {
        formState.value = formState.value?.copy(cfPassword = text)
    }

    private fun checkPhoneValid(): Boolean {
        return if (!RegexUtils.isPhoneNumberValid(formState.value?.phone)) {
            phoneError.value = R.string.forgot_pass_error_phone
            false
        } else {
            phoneError.value = null
            true
        }
    }

    fun phoneDataChanged(text: String) {
        formState.value = formState.value?.copy(phone = text)
    }

    private fun checkQuestionValid(): Boolean {
        return if (!RegexUtils.isQuestionValid(formState.value?.question)) {
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
        return if (!RegexUtils.isCityValid(formState.value?.city)) {
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

    private fun checkGenderValid(): Boolean {
        return if (!RegexUtils.isGenderValid(formState.value?.gender)) {
            genderError.value = R.string.forgot_pass_error_secret_question
            false
        } else {
            genderError.value = null
            true
        }
    }

    fun genderDataChanged(text: String) {
        formState.value = formState.value?.copy(gender = text)
    }

    private fun checkAnswerValid(): Boolean {
        return if (!RegexUtils.isAnswerValid(formState.value?.answer)) {
            answerError.value = R.string.forgot_pass_error_secret_answer
            false
        } else {
            answerError.value = null
            true
        }
    }

    fun answerDataChanged(text: String) {
        formState.value = formState.value?.copy(answer = text)
    }

    private fun checkNameValid(): Boolean {
        var errors = arrayOfNulls<Number>(2)

        val kanaFirstName = formState.value?.kanaFirstName
        val kanaLastName = formState.value?.kanaLastName

        val hiraFirstName = formState.value?.hiraFirstName
        val hiraLastName = formState.value?.hiraLastName

        if (kanaFirstName.isNullOrEmpty() || kanaLastName.isNullOrEmpty()
            || !RegexUtils.isKanaNameFullWidth("$kanaFirstName　$kanaLastName")
        ) {
            kanaNameError.value = true
            errors[0] = R.string.rgx_error_name_kana_full_width
        } else {
            kanaNameError.value = false
        }

        if (hiraFirstName.isNullOrEmpty() || hiraLastName.isNullOrEmpty()
            || !RegexUtils.isNameFullWidth("$hiraFirstName　$hiraLastName")
        ) {
            furiganaNameError.value = true
            errors[1] = R.string.rgx_error_name_full_width
        } else {
            furiganaNameError.value = false
        }

        errors = errors.filterNotNull().toTypedArray()
        nameError.value = if (errors.isNullOrEmpty()) null else errors
        return errors.isNullOrEmpty()
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

    private fun isDateValid(date: String?): Boolean {
        return Converter.isDateValid(date)
    }

    private fun isCfPasswordValid(password: String?, cfPassword: String?): Boolean {
        return RegexUtils.isPasswordValid(cfPassword) && cfPassword == password
    }

    fun submit() {
        val isValidUsername = checkUsernameValid()
        val isValidLoginId = checkLoginIdValid()
        val isValidDate = checkDateValid()
        val isValidPhone = checkPhoneValid()
        val isValidQuestion = checkQuestionValid()
        val isValidCity = checkCityValid()
        val isValidGender = checkGenderValid()
        val isValidAnswer = checkAnswerValid()
        val isValidPassword = checkPasswordValid()
        val isValidCfPassword = checkCfPasswordValid()
        val isValidName = checkNameValid()
        if (isValidUsername && isValidLoginId &&
            isValidDate && isValidPhone &&
            isValidQuestion && isValidCity &&
            isValidGender && isValidAnswer &&
            isValidPassword && isValidCfPassword && isValidName
        ) {
            formResultState.value = SignupInputResultState(success = true)
        }
    }

    fun checkValidForm(): Boolean {
        val isValid = formState.value?.let { form ->
            val fields = arrayOf(
                form.nickname,
                form.loginId,
                form.date,
                form.phone,
                form.question,
                form.city,
                form.gender,
                form.answer,
                form.password,
                form.cfPassword,
                form.kanaFirstName,
                form.kanaLastName,
                form.hiraFirstName,
                form.hiraLastName
            )
            val valid = !fields.any { it.isNullOrEmpty() }
            valid
        } ?: false
        validForm.value = isValid
        return isValid
    }

}