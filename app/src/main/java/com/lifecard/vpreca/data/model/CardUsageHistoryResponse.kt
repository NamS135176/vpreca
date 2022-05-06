package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardUsageHistoryResponse(
    @SerializedName("items")
    val items: List<CardUsageHistory>,
)