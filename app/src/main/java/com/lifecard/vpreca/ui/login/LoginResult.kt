package com.lifecard.vpreca.ui.login

import com.lifecard.vpreca.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)