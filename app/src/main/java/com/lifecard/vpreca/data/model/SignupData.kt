package com.lifecard.vpreca.data.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignupData(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}