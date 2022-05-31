package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class MemberResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: MemberBrandResponse,
)

data class MemberBrandResponse(
    @SerializedName("response")
    val response: MemberResponseContent,
)

data class MemberResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: MemberInfo,
    @SerializedName("memberSubInfo")
    val memberSubInfo: MemberSubInfo,
    @SerializedName("creditCardInfo")
    val creditCardInfo: CreditCardInfo,
)