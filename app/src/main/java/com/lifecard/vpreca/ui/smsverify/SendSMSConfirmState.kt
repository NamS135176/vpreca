package com.lifecard.vpreca.ui.smsverify

import com.lifecard.vpreca.data.model.SMSAuthResponseContent
import com.lifecard.vpreca.exception.ErrorMessageException

data class SendSMSConfirmState(
    val success: SMSAuthResponseContent? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
