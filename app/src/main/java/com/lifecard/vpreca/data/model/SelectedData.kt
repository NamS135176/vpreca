package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SelectedData(
    @SerializedName("isSelected")
    var isSelected: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("isFirst")
    var isFirst: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}