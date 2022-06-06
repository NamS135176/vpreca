package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class IssueGiftReqWithoutCardRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardInfoOnlyDesignId,
    @SerializedName("chargeInfo")
    val chargeInfo: GiftNumberRequestContentInfo
)

data class CardInfoOnlyDesignId(
    @SerializedName("designId")
    val designId: String,
)
