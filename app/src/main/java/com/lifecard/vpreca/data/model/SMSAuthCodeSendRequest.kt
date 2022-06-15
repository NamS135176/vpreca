package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SMSAuthCodeSendRequest(
    @SerializedName("memberInfo")
    val memberInfo: SMSAuthCodeMemberInfoContent,
    @SerializedName("certInfo")
    val certInfo: SMSAuthCodeCertInfoContent,
)

data class SMSAuthCodeMemberInfoContent(
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("telephoneNumber")
    val telephoneNumber: String,
)

data class SMSAuthCodeCertInfoContent(
    @SerializedName("certType")
    val certType: String,
    @SerializedName("operationType")
    val operationType: String,
    @SerializedName("certSumFlg")
    val certSumFlg: String,
    @SerializedName("operationSumFlg")
    val operationSumFlg: String,
)

