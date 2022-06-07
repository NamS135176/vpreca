package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ChangeInfoMemberRequest(
    @SerializedName("memberInfo")
    val memberInfo: ChangeInfoMemberData,
)




