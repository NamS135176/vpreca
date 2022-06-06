package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDesignData(
    @SerializedName("cardExplain")
    val cardExplain: String,
    @SerializedName("cardImageFile")
    val cardImageFile: String,
    @SerializedName("cardSchemeInfo")
    val cardSchemeInfo: String,
    @SerializedName("designId")
    val designId: String,
    @SerializedName("designName")
    val designName: String,
    @SerializedName("showPriority")
    val showPriority: String,
    @SerializedName("thumbnailCardImageFile")
    val thumbnailCardImageFile: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
