package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class GiftNumberAuthReqResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: GiftNumberAuthReqBrandResponse,
)

data class GiftNumberAuthReqBrandResponse(
    @SerializedName("response")
    val response: GiftNumberAuthReqResponseContent,
)

data class GiftNumberAuthReqResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("giftNumberInfo")
    val giftNumberInfo:GiftInfo?,

    )