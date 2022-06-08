package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardInfoRequestContent(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardInfoRequestContentInfo
)

data class CardInfoWithouMemberRequestContent(
    @SerializedName("cardInfo")
    val cardInfo: CardInfoRequestContentInfo
)

data class MemberInfoContent(
    @SerializedName("memberNumber")
    val memberNumber: String,
)


data class CardInfoRequestContentInfo(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("vcn")
    val vcn: String,
)
