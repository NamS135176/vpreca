package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class FeeSelReqRequest(
    @SerializedName("feeInfo")
    val feeInfo: FeeSelReqRequestContentInfo
)

data class FeeSelReqRequestContentInfo(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("feeType")
    val feeType: String,
    @SerializedName("targetAmount")
    val targetAmount: String,
)
