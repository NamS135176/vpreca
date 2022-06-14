package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName
import com.lifecard.vpreca.utils.requestDate
import java.util.*

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
    val requestDate: String = Date().requestDate(),
    @SerializedName("searchKey")
    val searchKey: String = "",
    @SerializedName("messageDigest")
    val messageDigest: String = "",
    @SerializedName("requestReceiveDate")
    val requestReceiveDate: String = "",
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