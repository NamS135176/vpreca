package com.lifecard.vpreca.ui.forgotpass

data class ForgotPassState(
    val emailError: Int? = null,
    val dateError: Int? = null,
    val phoneError: Int? = null,
    val questionError: Int? = null,
    val answerError: Int? = null
)
