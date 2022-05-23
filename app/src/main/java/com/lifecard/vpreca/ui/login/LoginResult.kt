package com.lifecard.vpreca.ui.login

import com.lifecard.vpreca.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val navigateSmsVerify: Boolean? = false,
    val navigateUpdateAccount: Boolean? = false,
    val error: Int? = null,
    val errorText: String? = null
)