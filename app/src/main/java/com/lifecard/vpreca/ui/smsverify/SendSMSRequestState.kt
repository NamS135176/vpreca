package com.lifecard.vpreca.ui.smsverify

import com.lifecard.vpreca.data.model.SendSMSResponseContent
import com.lifecard.vpreca.exception.ErrorMessageException

data class SendSMSRequestState(
    val success: SendSMSResponseContent? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
