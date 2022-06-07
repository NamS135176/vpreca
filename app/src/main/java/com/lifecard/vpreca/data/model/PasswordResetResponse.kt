package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class PasswordResetResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: PasswordUpdateBrandResponse,
)

data class PasswordResetBrandResponse(
    @SerializedName("response")
    val response: PasswordResetResponseContent,
)

data class PasswordResetResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
)