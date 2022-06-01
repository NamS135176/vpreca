package com.lifecard.vpreca.ui.balance_amount

import com.lifecard.vpreca.data.model.SuspendDeal
import com.lifecard.vpreca.exception.ErrorMessageException

data class SuspendDealResult(
    val success: List<SuspendDeal>? = null,
    val error: ErrorMessageException? = null,
)