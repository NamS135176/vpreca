package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequestContent(
    @SerializedName("loginInfo")
    val loginInfo: LoginRequestContentInfo
)

data class LoginRequestContentInfo(
    @SerializedName("loginPassword")
    val loginPassword: String,
    @SerializedName("loginId")
    val loginId: String,
)
