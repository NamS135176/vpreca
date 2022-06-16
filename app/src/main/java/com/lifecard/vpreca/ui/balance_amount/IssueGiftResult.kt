package com.lifecard.vpreca.ui.balance_amount

import com.lifecard.vpreca.data.model.IssueGiftReqResponseContent
import com.lifecard.vpreca.exception.ErrorMessageException

data class IssueGiftResult(
    val success: IssueGiftReqResponseContent? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
