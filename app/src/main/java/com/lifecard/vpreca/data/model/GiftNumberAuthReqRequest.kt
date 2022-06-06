package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class GiftNumberAuthReqRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("giftNumberInfo")
    val giftNumberInfo: giftNumberRequestContentInfo
)


data class giftNumberRequestContentInfo(
    @SerializedName("giftNumber")
    val giftNumber: String,
)
