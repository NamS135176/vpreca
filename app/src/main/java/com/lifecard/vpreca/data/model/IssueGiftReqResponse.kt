package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class IssueGiftReqResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: IssueGiftReqBrandResponse,
)

data class IssueGiftReqBrandResponse(
    @SerializedName("response")
    val response: IssueGiftReqResponseContent,
)

data class IssueGiftReqResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardInfo")
    val cardInfo: CardInfo?,
    @SerializedName("chargeInfo")
    val chargeInfo: ChargeInfo?,
    @SerializedName("overdraftAdjustInfo")
    val overdraftAdjustInfo: OverdraftAdjustInfo?,
)

data class ChargeInfo(
    @SerializedName("noveltyId")
    val noveltyId: String,
    @SerializedName("giftNumber")
    val giftNumber: String,
    @SerializedName("updateAmount")
    val updateAmount: String,
)

data class OverdraftAdjustInfo(
    @SerializedName("adjustCount")
    val adjustCount: String,
    @SerializedName("adjustFee")
    val adjustFee: String,
    @SerializedName("beforeUnadjustDifferenceTotalAmount")
    val beforeUnadjustDifferenceTotalAmount: String,
    @SerializedName("afterUnadjustDifferenceTotalAmount")
    val afterUnadjustDifferenceTotalAmount: String,
)