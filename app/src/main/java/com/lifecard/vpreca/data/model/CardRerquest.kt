package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CardListRequestContent(
    @SerializedName("memberInfo")
    val memberInfo: CardListRequestMemberInfo,
    @SerializedName("invalidCardResFlg")
    val invalidCardResFlg: String = "0",

    )

data class CardListRequestMemberInfo(
    @SerializedName("memberNumber")
    val memberNumber: String
)
