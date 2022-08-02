package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lifecard.vpreca.R
import kotlinx.parcelize.Parcelize

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

fun DesignCard?.getBackgroundCard(): Int{
    var draw = 0
    when (this?.designId) {
        "99033" -> draw = R.drawable.first
        "99455" -> draw = R.drawable.second
        "99999" -> draw = R.drawable.third
        else -> draw = R.drawable.first
    }
    return draw
}

fun DesignCard?.getBackgroundCardSelected(): Int{
    var draw = 0
    when (this?.designId) {
        "99033" -> draw = R.drawable.first
        "99455" -> draw = R.drawable.second
        "99999" -> draw = R.drawable.third
        else -> draw = R.drawable.first
    }
    return draw
}