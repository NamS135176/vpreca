package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ChangeInfoMemberResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: ChangeInfoMemberBrandResponse,
)

data class ChangeInfoMemberBrandResponse(
    @SerializedName("response")
    val response: ChangeInfoMemberResponseContent,
)

data class ChangeInfoMemberResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: ChangeInfoMemberData?,
)