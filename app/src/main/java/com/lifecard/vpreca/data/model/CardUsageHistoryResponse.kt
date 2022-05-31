package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardUsageHistoryResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: CardUsageHistoryBrandResponse,
)

data class CardUsageHistoryBrandResponse(
    @SerializedName("response")
    val response: CardUsageHistoryResponseContent,
)

data class CardUsageHistoryResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("selectHitRecordCount")
    val selectHitRecordCount: Int,
    @SerializedName("responseRecordCount")
    val responseRecordCount: Int,
    @SerializedName("cardDealHistoryInfo")
    val cardInfo: List<CardUsageHistory>,
)