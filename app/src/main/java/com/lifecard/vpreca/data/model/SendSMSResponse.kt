package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SendSMSResponse(
    @SerializedName("response")
    val response: SendSMSResponseContent,
)

data class SendSMSResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("extCertDealId")
    val extCertDealId: String?,
    @SerializedName("certType")
    val certType: String?,
    @SerializedName("ivrTelephoneNumber")
    val ivrTelephoneNumber: String?,
)