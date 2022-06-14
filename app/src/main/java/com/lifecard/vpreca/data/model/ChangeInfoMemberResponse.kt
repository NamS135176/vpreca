package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ChangeInfoMemberResponse(
    @SerializedName("response")
    val response: ChangeInfoMemberResponseContent,
)

data class ChangeInfoMemberResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: ChangeInfoMemberData?,
)