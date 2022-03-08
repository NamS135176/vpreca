package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardResponse (
    @SerializedName("data")
    val data: List<CreditCard>,
)