package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName
import com.lifecard.vpreca.utils.requestDate
import java.util.*

//data class BrandRequest(
//    @SerializedName("brandPrecaApi")
//    val brandPrecaApi: Request,
//)

data class Request(
    @SerializedName("request")
    val request: Any,
    @SerializedName("head")
    val head: BaseHead,
)


data class BaseHead(
    @SerializedName("messageType")
    val messageType: String,
    @SerializedName("centerId")
    val centerId: String = "",
    @SerializedName("nodeId")
    val nodeId: String = "",
    @SerializedName("brandSchemeId")
    val brandSchemeId: String = "",
    @SerializedName("processId")
    val processId: String = "",
    @SerializedName("requestDate")
//    val requestDate: String = Date().requestDate(),
    val requestDate: String = "20220531083300",//TODO remove hard code
    @SerializedName("searchKey")
    val searchKey: String = "",
    @SerializedName("messageDigest")
    val messageDigest: String? = null,
)

enum class MessageType(val value: String) {
    LoginReq("LoginReq"),
    Logout("Logout"),
    MemberSelReq("MemberSelReq"),
    CardListSelReq("CardListSelReq"),
    CardSelReq("CardSelReq"),
    SuspendDealSelReq("SuspendDealSelReq"),
    GiftNumberAuthReq("GiftNumberAuthReq"),
    MemberListSelReq("MemberListSelReq"),
    CardDealHisReq("CardDealHisReq"),
    FeeSelReq("FeeSelReq"),
    CardDesignSelReq("CardDesignSelReq"),
    MemberRegReq("MemberRegReq"),
    MemberUpdReq("MemberUpdReq"),
    PasswordResetReq("PasswordResetReq"),
    PasswordUpdReq("PasswordUpdReq"),
    CellNumCertSendReq("CellNumCertSendReq"),
    TelNumberCertReq("TelNumberCertReq"),
    MailAddrCertSendReq("MailAddrCertSendReq"),
    MailAddrCertReq("MailAddrCertReq"),
    CardRelationRegReq("CardRelationRegReq"),
    CardUpdReq("CardUpdReq"),
    CardRepublishReq("CardRepublishReq"),
    IssueGiftReq("IssueGiftReq"),
    IssueSumReq("IssueSumReq"),
    IssueBankReq("IssueBankReq"),
    IssueSumBankReq("IssueSumBankReq"),
    DealAdjustReq("DealAdjustReq"),
    IssueGiftSumReq("IssueGiftSumReq"),
    SｍsIvrAuthCodeSendReq("SｍsIvrAuthCodeSendReq"),
    SｍsIvrAuthReq("SｍsIvrAuthReq"),
    CardCertifiNoMemReq("CardCertifiNoMemReq"),
    NoticeSelReq("NoticeSelReq")
}