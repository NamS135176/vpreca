package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GiftInfo(
    @SerializedName("giftAmount")
    val giftAmount: String,
    @SerializedName("giftNumber")
    val giftNumber: String,
    @SerializedName("giftUsedFlg")
    val giftUsedFlg: String,
    @SerializedName("noveltyEndDate")
    val noveltyEndDate: String,
    @SerializedName("noveltyId")
    val noveltyId: String,
    @SerializedName("noveltyStartDate")
    val noveltyStartDate: String
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}