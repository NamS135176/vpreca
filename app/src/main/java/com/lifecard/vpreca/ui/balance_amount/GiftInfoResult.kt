package com.lifecard.vpreca.ui.balance_amount

import com.lifecard.vpreca.data.model.GiftInfo
import com.lifecard.vpreca.exception.ErrorMessageException

data class GiftInfoResult(
    val success: GiftInfo? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
