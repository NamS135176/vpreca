package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SuspendDealResponse(
    @SerializedName("response")
    val response: SuspendDealResponseContent,
)

data class SuspendDealResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("recordCount")
    val recordCount: Int?,
    @SerializedName("suspendDeal")
    val suspendDeal: List<SuspendDeal>?,
)