package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class IssueGiftReqWithCardRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardInfoWithCard,
    @SerializedName("chargeInfo")
    val chargeInfo: GiftNumberRequestContentInfo
)

data class CardInfoWithCard(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("designId")
    val designId: String,
    @SerializedName("cardNickname")
    val cardNickname: String,
    @SerializedName("vcnName")
    val vcnName: String,
)
