package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class IssueSumReqResponse(
    @SerializedName("response")
    val response: IssueSumReqResponseContent,
)

data class IssueSumReqResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val cardInfo: CreditCard?,
    @SerializedName("sumUpInfo")
    val sumUpInfo: SumUpInfoResponseContent?,
    @SerializedName("sumUpSrcCardInfo")
    val sumUpSrcCardInfo: SumUpInfoCardInfoResponseContent?,
)

data class SumUpInfoResponseContent(
    @SerializedName("sumUpAmount")
    val sumUpAmount: String,
    @SerializedName("feeAmount")
    val feeAmount: String,
    @SerializedName("sumUpSrcCardCount")
    val sumUpSrcCardCount: String,
)

data class SumUpInfoCardInfoResponseContent(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("vcn")
    val vcn: String,
    @SerializedName("vcnCardId")
    val vcnCardId: String,
    @SerializedName("updateAmount")
    val updateAmount: String,
)