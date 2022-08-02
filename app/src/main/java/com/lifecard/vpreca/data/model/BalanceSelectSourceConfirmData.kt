package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceSelectSourceConfirmData(
    @SerializedName("balanceAmount")
    val balanceAmount: String,
    @SerializedName("select")
    val select: String,
    @SerializedName("remain")
    val remain: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
