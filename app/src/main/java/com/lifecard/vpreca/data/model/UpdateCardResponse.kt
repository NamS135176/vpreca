package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class UpdateCardResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: UpdateCardBrandResponse,
)

data class UpdateCardBrandResponse(
    @SerializedName("response")
    val response: UpdateCardResponseContent,
)

data class UpdateCardResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val creditCard: CreditCard,
)