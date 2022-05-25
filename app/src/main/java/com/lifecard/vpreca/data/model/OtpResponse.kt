package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("otp")
    val otp: String
)