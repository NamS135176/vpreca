package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ChangeInfoMemberResponse(
    @SerializedName("response")
    val response: MemberResponseContent,
)