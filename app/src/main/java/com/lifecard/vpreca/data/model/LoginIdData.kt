package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginIdData(
    @SerializedName("loginId")
    val loginId: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
