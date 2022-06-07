package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class BalanceGiftData(
    @SerializedName("balanceAmount")
    val balanceAmount: String,
    @SerializedName("giftAmount")
    val giftAmount: String,
    @SerializedName("giftNumber")
    val giftNumber: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}