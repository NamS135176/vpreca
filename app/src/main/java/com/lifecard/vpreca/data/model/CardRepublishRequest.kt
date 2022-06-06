package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardRepublishRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardRepublishRequestContentInfo
)


data class CardRepublishRequestContentInfo(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("vcn")
    val vcn: String,
    @SerializedName("cooperatorNumber")
    val cooperatorNumber: String?,
)
