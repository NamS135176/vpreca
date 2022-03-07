package com.lifecard.vpreca.ui.login

import com.lifecard.vpreca.data.Result

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)