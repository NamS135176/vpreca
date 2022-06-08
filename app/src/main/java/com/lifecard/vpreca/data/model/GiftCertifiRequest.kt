package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class GiftCertifiRequest(
    @SerializedName("cardInfo")
    val cardInfo: GiftCertifiCardInfoRequestContentInfo
)


data class GiftCertifiCardInfoRequestContentInfo(
    @SerializedName("confirmationNumber")
    val confirmationNumber: String,
    @SerializedName("vcnFourDigits")
    val vcnFourDigits: String,
)
