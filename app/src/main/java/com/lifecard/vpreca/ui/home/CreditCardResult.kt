package com.lifecard.vpreca.ui.home

import com.lifecard.vpreca.data.model.CreditCard

data class CreditCardResult(
    val success: List<CreditCard>? = null,
    val error: Int? = null
)