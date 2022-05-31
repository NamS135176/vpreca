package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CreditCardInfo(
    @SerializedName("bgOneClickDbKey")
    val bgOneClickDbKey: String?,
    @SerializedName("creditBrandName")
    val creditBrandName: String?,
    @SerializedName("creditCardExpirationDate")
    val creditCardExpirationDate: String?,
    @SerializedName("creditCardKind")
    val creditCardKind: String?,
    @SerializedName("creditCardNumber4Digit")
    val creditCardNumber4Digit: String?,
)