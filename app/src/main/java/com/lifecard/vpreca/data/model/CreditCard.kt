package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class CreditCard(
    @SerializedName("id")
    val id: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("last4")
    val last4: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("amount")
    val amount: Int,
)