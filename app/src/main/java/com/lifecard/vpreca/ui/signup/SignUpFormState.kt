package com.lifecard.vpreca.ui.signup

data class SignUpFormState(
    val idError: Int? = null,
    val usernameError: Int? = null,
    val dateError: Int? = null,
    val phoneError: Int? = null,
    val questionError: Int? = null,
    val cityError: Int? = null,
    val genderError: Int? = null,
    val answerError: Int? = null,
    val passwordError: Int? = null,
    val cfPasswordError: Int? = null
)
