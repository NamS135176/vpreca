package com.lifecard.vpreca.ui.login

import com.lifecard.vpreca.data.model.MemberInfo

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: MemberInfo? = null,
    val error: Int? = null,
    val errorText: String? = null
)