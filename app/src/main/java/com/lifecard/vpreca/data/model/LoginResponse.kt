package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: LoginBrandResponse,
)

data class LoginBrandResponse(
    @SerializedName("response")
    val response: MemberResponseContent,
)