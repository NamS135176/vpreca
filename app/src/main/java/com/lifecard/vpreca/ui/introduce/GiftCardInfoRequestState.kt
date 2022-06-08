package com.lifecard.vpreca.ui.introduce

import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.exception.ErrorMessageException

data class GiftCardInfoRequestState(
    val success: CardInfo? = null,
    val errorText: String? = null,
    val error: ErrorMessageException? = null,
    val networkTrouble: Boolean? = false,
    val internalError: String? = null
)
