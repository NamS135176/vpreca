package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class GiftCertifiResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: GiftCertifiBrandResponse,
)

data class GiftCertifiBrandResponse(
    @SerializedName("response")
    val response: GiftCertifiResponseContent,
)

data class GiftCertifiResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("cardSchemeId")
    val cardSchemeId: String?,
    @SerializedName("precaNumber")
    val precaNumber: String?,
    @SerializedName("vcn")
    val vcn: String?,
    @SerializedName("vcnName")
    val vcnName: String?,
    @SerializedName("vcnExpirationDate")
    val vcnExpirationDate: String?,
    @SerializedName("publishType")
    val publishType: String?,
    @SerializedName("cardStatus")
    val cardStatus: String?,
    @SerializedName("cardImageFile")
    val cardImageFile: String?,
)