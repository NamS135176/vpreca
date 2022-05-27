package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DesignCard(
    @SerializedName("designId")
    val designId: String,
    @SerializedName("isSelected")
    var isSelected: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}