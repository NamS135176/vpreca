package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VprecaCard(
    @SerializedName("color")
    val color: String,
    @SerializedName("vcnSecurityLockFlg")
    val vcnSecurityLockFlg: String,
    @SerializedName("price")
    val price: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}