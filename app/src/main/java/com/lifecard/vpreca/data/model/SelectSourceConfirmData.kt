package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectSourceConfirmData(
    @SerializedName("balanceAmount")
    val balanceAmount: String,
    @SerializedName("select")
    val select: String,
    @SerializedName("remain")
    val remain: String,
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
    @SerializedName("designId")
    val designId: String,
    @SerializedName("cardNickname")
    val cardNickname: String,
    @SerializedName("vcnName")
    val vcnName: String,
    @SerializedName("precaNumber")
    val precaNumber: String,
    @SerializedName("vcn")
    val vcn: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
