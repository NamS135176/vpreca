package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SendSMSRequest(
    @SerializedName("loginId")
    val loginId: String,
)
