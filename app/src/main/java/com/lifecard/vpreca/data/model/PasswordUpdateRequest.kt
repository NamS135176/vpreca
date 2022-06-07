package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class PasswordUpdateRequest(
    @SerializedName("memberInfo")
    val memberInfo: PasswordUpdateMemberInfoContent,
)

data class PasswordUpdateMemberInfoContent(
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("passwordResetType")
    val passwordResetType: String,
    @SerializedName("loginPasswordCur")
    val loginPasswordCur: String,
    @SerializedName("loginPasswordNew")
    val loginPasswordNew: String,
)
