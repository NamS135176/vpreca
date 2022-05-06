package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardResponse (
    @SerializedName("items")
    val data: List<CreditCard>,
)