package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.MemberInfo
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
    val nameError = MutableLiveData<Array<Number?>?>()
    val kanaNameError = MutableLiveData<Boolean>()
    val furiganaNameError = MutableLiveData<Boolean>()
    val formResultState = MutableLiveData<ChangeInfoInputResultState?>()
    val formState = MutableLiveData(ChangeInfoInputState())

    val validForm = MutableLiveData<Boolean>()

    var originUserData: MemberInfo? = null

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
        val email1 = formState.value?.email1
        return if (!RegexUtils.isEmailValid(email1)) {
            email1Error.value = R.string.rgx_error_email
            false
        } else if (email1 == originUserData?.mailAddress2) {
            email1Error.value = R.string.rgx_error_email_duplicate
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
        } else if (email2 == originUserData?.mailAddress1) {
            email2Error.value = R.string.rgx_error_email_duplicate
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
            email1ConfirmError.value = R.string.rgx_error_email_confirm_not_match
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
            email2ConfirmError.value = R.string.rgx_error_email_confirm_not_match
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
            false
        } else {
            answerError.value = null
            true
        }
    }

    fun answerDataChanged(text: String) {
        formState.value = formState.value?.copy(answer = text)
    }

    private fun changeNameValid(): Boolean {
        var errors = arrayOfNulls<Number>(2)

        val kanaFirstName = formState.value?.kanaFirstName
        val kanaLastName = formState.value?.kanaLastName

        val hiraFirstName = formState.value?.hiraFirstName
        val hiraLastName = formState.value?.hiraLastName

        if (kanaFirstName.isNullOrEmpty() || kanaLastName.isNullOrEmpty()
            || !RegexUtils.isKanaNameFullWidth("$kanaFirstName???$kanaLastName")
        ) {
            errors[0] = R.string.rgx_error_name_kana_full_width
            kanaNameError.value = true
        } else {
            kanaNameError.value = false
        }
        if (hiraFirstName.isNullOrEmpty() || hiraLastName.isNullOrEmpty()
            || !RegexUtils.isNameFullWidth("$hiraFirstName???$hiraLastName")
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

    fun kanaFirstNameDataChanged(text: String?) {
        formState.value = formState.value?.copy(kanaFirstName = text)
    }

    fun kanaLastNameDataChanged(text: String?) {
        formState.value = formState.value?.copy(kanaLastName = text)
    }

    fun hiraFirstNameDataChanged(text: String?) {
        formState.value = formState.value?.copy(hiraFirstName = text)
    }

    fun hiraLastNameDataChanged(text: String?) {
        formState.value = formState.value?.copy(hiraLastName = text)
    }

    private fun isQuestionValid(question: String?): Boolean {
        return !question.isNullOrEmpty()
    }

    private fun isCityValid(city: String?): Boolean {
        return !city.isNullOrEmpty()
    }

    private fun isAnswerValid(answer: String?): Boolean {
        return RegexUtils.isAnswerValid(answer = answer)
    }

    private fun isEmail1ConfirmValid(cfEmail: String?, email: String?): Boolean {
//        return RegexUtils.isEmailValid(cfEmail) && email == cfEmail
        return email == cfEmail//only check not match
    }

    private fun isEmail2ConfirmValid(cfEmail: String?, email: String?): Boolean {
        if (email.isNullOrEmpty()) {
            return true
        } else {
//            return RegexUtils.isEmailValid(email) && email == cfEmail
            return email == cfEmail//only check not match
        }

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
        val isValidName = changeNameValid()
        if (isValidEmail1 && isValidEmail1Confirm &&
            isValidEmail2 && isValidEmail2Confirm &&
            isValidNickname && isValidLoginId &&
            isValidQuestion && isValidCity &&
            isValidAnswer && isValidName
        ) {
            formResultState.value = ChangeInfoInputResultState(success = true)
        }
    }

    fun checkValidForm(): Boolean {
        val isValid = formState.value?.let { form ->
            val fields = arrayOf(
                form.nickname,
                form.loginId,
                form.question,
                form.city,
                form.answer,
                form.email1,
                form.email1Confirm,
                form.kanaFirstName,
                form.kanaLastName,
                form.hiraFirstName,
                form.hiraLastName
            )
            !fields.any { it.isNullOrEmpty() }
        } ?: false
        validForm.value = isValid
        return isValid
    }

}