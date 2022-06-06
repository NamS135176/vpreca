package com.lifecard.vpreca.ui.balance_amount

import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.FeeInfo
import com.lifecard.vpreca.exception.ErrorMessageException

data class FeeInfoResult(
    val success: CreditCard? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
