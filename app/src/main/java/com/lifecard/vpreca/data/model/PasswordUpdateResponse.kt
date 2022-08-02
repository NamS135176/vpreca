package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class PasswordUpdateResponse(
    @SerializedName("response")
    val response: PasswordUpdateResponseContent,
)

data class PasswordUpdateResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: MemberInfo?,
)