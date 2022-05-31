package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class MemberRequestContent(
    @SerializedName("memberInfo")
    val memberInfo: MemberRequestContentInfo
)

data class MemberRequestContentInfo(
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("loginId")
    val loginId: String,
)
