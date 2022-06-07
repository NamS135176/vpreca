package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PhoneData(
    @SerializedName("phone")
    var phone: String,
    @SerializedName("memberNumber")
    var memberNumber: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}