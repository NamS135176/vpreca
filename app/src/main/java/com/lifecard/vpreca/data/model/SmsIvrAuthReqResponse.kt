package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SmsIvrAuthReqResponse(
    @SerializedName("response")
    val response: SmsIvrAuthReqResponseContent,
)

data class SmsIvrAuthReqResponseContent(
    @SerializedName("resultCode")
    val resultCode: String
)