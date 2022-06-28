package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SmsIvrAuthRequest(
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("certCode")
    val certCode: String,
    @SerializedName("extCertDealId")
    val extCertDealId: String,
    @SerializedName("certType")
    val certType: String,
)
