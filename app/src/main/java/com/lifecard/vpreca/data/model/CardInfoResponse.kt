package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardInfoResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: CardInfoBrandResponse,
)

data class CardInfoBrandResponse(
    @SerializedName("response")
    val response: CardInfoResponseContent,
)

data class CardInfoResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val cardInfo: CardInfo,
)