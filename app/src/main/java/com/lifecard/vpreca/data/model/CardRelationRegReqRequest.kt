package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardRelationRegReqRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardRelationRegRequestContentInfo
)




data class CardRelationRegRequestContentInfo(
    @SerializedName("vcn")
    val vcn: String,
    @SerializedName("vcnExpirationDate")
    val vcnExpirationDate: String,
    @SerializedName("vcnSecurityCode")
    val vcnSecurityCode: String,
    @SerializedName("cardNickname")
    val cardNickname: String,
)
