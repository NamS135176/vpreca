package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: BaseBrandResponse,
) {
    override fun toString(): String {
        return "BaseResponse (brandPrecaApi: ${brandPrecaApi})"
    }
}

data class BaseBrandResponse(
    @SerializedName("response")
    val response: BaseResponseContent,
) {
    override fun toString(): String {
        return "BaseBrandResponse (response: ${response})"
    }
}

data class BaseResponseContent(
    @SerializedName("resultCode")
    val resultCode: String
) {
    override fun toString(): String {
        return "BaseResponseContent (resultCode: ${resultCode})"
    }
}