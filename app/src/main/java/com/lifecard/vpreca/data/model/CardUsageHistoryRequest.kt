package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardUsageHistoryRequestContent(
    @SerializedName("memberInfo")
    val memberInfo: CardUsageMemberInfoRequest,
    @SerializedName("cardInfo")
    val cardInfo: CardUsageCardInfoRequest,
    @SerializedName("startLine")
    val startLine: Int = 1,
    @SerializedName("endLine")
    val endLine: Int = 1,
    @SerializedName("cardUseHisSelTerm")
    val cardUseHisSelTerm: Int? = null,
)

data class CardUsageMemberInfoRequest(
    @SerializedName("memberNumber")
    val memberNumber: String,
)

data class CardUsageCardInfoRequest(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("vcn")
    val vcn: String? = null,
)