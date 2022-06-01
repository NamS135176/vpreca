package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SuspendDealRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
)
