package com.lifecard.vpreca.ui.smsverify

import com.lifecard.vpreca.data.model.SMSAuthCodeSendResponseContent
import com.lifecard.vpreca.exception.ErrorMessageException

data class SendSMSRequestState(
    val success: SMSAuthCodeSendResponseContent? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
