package com.lifecard.vpreca.ui.home

import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ErrorMessageException

data class CreditCardResult(
    val success: List<CreditCard>? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)