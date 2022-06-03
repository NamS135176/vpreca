package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: CardBrandResponse,
)

data class CardBrandResponse(
    @SerializedName("response")
    val response: CardResponseContent,
)

data class CardResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val cardInfo: List<CreditCard>?,
    @SerializedName("recordCount")
    val recordCount: Int?,
)