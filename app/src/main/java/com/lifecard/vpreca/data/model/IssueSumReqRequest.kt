package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class IssueSumReqRequest(
    @SerializedName("feeInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardInfoWithDesignIdContentInfo,
    @SerializedName("sumUpInfo")
    val sumUpInfo: SumUpInfoContentInfo,
    @SerializedName("sumUpSrcCardInfo")
    val sumUpSrcCardInfo: CardInfoRequestContentInfo
)

data class CardInfoWithDesignIdContentInfo(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("designId")
    val designId: String,
    @SerializedName("cardNickname")
    val cardNickname: String,
    @SerializedName("vcnName")
    val vcnName: String,
)

data class SumUpInfoContentInfo(
    @SerializedName("feeAmount")
    val feeAmount: String,
    @SerializedName("feeGetResultType")
    val feeGetResultType: String,
    @SerializedName("feeCalculateType")
    val feeCalculateType: String,
    @SerializedName("feeInclusiveFlg")
    val feeInclusiveFlg: String,
    @SerializedName("sumUpSrcCardCount")
    val sumUpSrcCardCount: String,
)



