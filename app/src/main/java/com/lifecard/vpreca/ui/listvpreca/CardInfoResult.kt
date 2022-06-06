package com.lifecard.vpreca.ui.listvpreca

import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ErrorMessageException

data class CardInfoResult(
    val success: CardInfo? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)