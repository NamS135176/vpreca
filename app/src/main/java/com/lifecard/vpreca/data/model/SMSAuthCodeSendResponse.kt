package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class SMSAuthCodeSendResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: SMSAuthCodeSendBrandResponse,
)

data class SMSAuthCodeSendBrandResponse(
    @SerializedName("response")
    val response: SMSAuthCodeSendResponseContent,
)

data class SMSAuthCodeSendResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("memberInfo")
    val memberInfo: MemberInfo?,
    @SerializedName("extCertSetMst")
    val extCertSetMst: ExtCertSetMst?,
    @SerializedName("MemberCertReqHis")
    val MemberCertReqHis: MemberCertReqHis?,
    @SerializedName("certResInfo")
    val certResInfo: CertResInfo?,
)

data class ExtCertSetMst(
    @SerializedName("extCertCorpCd")
    val extCertCorpCd: String?,
    @SerializedName("certType")
    val certType: String?,
    @SerializedName("operationType")
    val operationType: String?,
    @SerializedName("lockJudgeType")
    val lockJudgeType: String?,
    @SerializedName("lockSpan")
    val lockSpan: String?,
    @SerializedName("lockThreshold")
    val lockThreshold: String?,
)

data class MemberCertReqHis(
    @SerializedName("memberNumber")
    val memberNumber: String?,
    @SerializedName("certSendCnt")
    val certSendCnt: String?,
    @SerializedName("certSendLastDate")
    val certSendLastDate: String?,
)

data class CertResInfo(
    @SerializedName("extCertDealId")
    val extCertDealId: String?,
)