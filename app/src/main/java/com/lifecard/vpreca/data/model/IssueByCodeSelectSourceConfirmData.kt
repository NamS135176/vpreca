package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class IssueByCodeSelectSourceConfirmData(
    @SerializedName("giftAmount")
    val giftAmount: String,
    @SerializedName("selectAmount")
    val selectAmount: String,
    @SerializedName("expireDate")
    val expireDate: Date,
    @SerializedName("pin")
    val pin: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("giftNumber")
    val giftNumber: String,
    @SerializedName("vcnName")
    val vcnName: String,
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("designId")
    val designId: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}