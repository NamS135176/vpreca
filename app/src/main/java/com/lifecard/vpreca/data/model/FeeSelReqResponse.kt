package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class FeeSelReqResponse(
    @SerializedName("response")
    val response: FeeSelReqResponseContent,
)

data class FeeSelReqResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("feeInfo")
    val feeInfo: List<FeeInfo>?,
    @SerializedName("recordCount")
    val recordCount: Int?,
)