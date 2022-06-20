package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SMSAuthResponse(
    @SerializedName("response")
    val response: SMSAuthResponseContent,
)

data class SMSAuthResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: MemberInfo?
)
