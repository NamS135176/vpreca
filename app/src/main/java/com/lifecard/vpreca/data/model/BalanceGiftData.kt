package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lifecard.vpreca.R
import kotlinx.parcelize.Parcelize

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

fun BalanceGiftData?.getBackgroundCard(): Int{
    var draw = 0
    when (this?.balanceAmount) {
        "99033" -> draw = R.drawable.first
        "99455" -> draw = R.drawable.second
        "99999" -> draw = R.drawable.third
        else -> draw = R.drawable.first
    }
    return draw
}
