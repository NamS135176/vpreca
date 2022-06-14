package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SMSAuthRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("certInfo")
    val certInfo: SMSAuthRequestContentInfo
)

data class SMSAuthRequestContentInfo(
    @SerializedName("certType")
    val certType: String,
    @SerializedName("operationType")
    val operationType: String,
    @SerializedName("certCode")
    val certCode: String,
    @SerializedName("extCertDealId")
    val extCertDealId: String,
)
