package com.lifecard.vpreca.ui.changeinfo

data class ChangeInfoInputState(
    val idError: Int? = null,
    val nicknameError: Int? = null,
    val questionError: Int? = null,
    val cityError: Int? = null,
    val answerError: Int? = null,
    val email1Error: Int? = null,
    val email1ConfirmError: Int? = null,
    val email2Error: Int? = null,
    val email2ConfirmError: Int? = null
)
