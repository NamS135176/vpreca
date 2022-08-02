package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("head")
    val head: BaseHeadResponse,
    @SerializedName("response")
    val response: BaseResponseContent,
) {
    override fun toString(): String {
        return "BaseBrandResponse (response: ${response})"
    }
}

data class BaseHeadResponse(
    @SerializedName("appliTermId")
    val appliTermId: String = "",
    @SerializedName("processId")
    val processId: String = "",
    @SerializedName("responseDate")
    val responseDate: String = "",
)

data class BaseResponseContent(
    @SerializedName("resultCode")
    val resultCode: String
) {
    override fun toString(): String {
        return "BaseResponseContent (resultCode: ${resultCode})"
    }
}