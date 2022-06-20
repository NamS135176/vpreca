package com.lifecard.vpreca.ui.forgotpass

import com.lifecard.vpreca.exception.ErrorMessageException

data class ResetPassReqState(
    val success: String? = null,
    val errorText: String? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)

