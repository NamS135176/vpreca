package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class PasswordResetRequest(
    @SerializedName("memberInfo")
    val memberInfo: PasswordResetMemberInfoContent,
)

data class PasswordResetMemberInfoContent(
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("mailAddress1")
    val mailAddress1: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("telephoneNumber1")
    val telephoneNumber1: String,
    @SerializedName("secretQuestion")
    val secretQuestion: String,
    @SerializedName("secretQuestionAnswer")
    val secretQuestionAnswer: String,
)
