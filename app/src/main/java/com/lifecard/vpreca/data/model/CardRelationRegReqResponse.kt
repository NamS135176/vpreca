package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardRelationRegReqResponse(
    @SerializedName("response")
    val response: CardRelationRegResponseContent,
)

data class CardRelationRegResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val cardInfo: CreditCard?,
)