package com.lifecard.vpreca.ui.login

import com.lifecard.vpreca.data.model.MemberInfo
import com.lifecard.vpreca.exception.ErrorMessageException

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: MemberInfo? = null,
    val errorText: String? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null,
    val smsVerification: Boolean? = false
)