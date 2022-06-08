package com.lifecard.vpreca.ui.introduce

import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.exception.ErrorMessageException

data class GiftCardRelationState(
    val success: CreditCard? = null,
    val errorText: String? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
