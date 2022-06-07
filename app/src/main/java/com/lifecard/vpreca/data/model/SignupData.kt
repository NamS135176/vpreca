package com.lifecard.vpreca.data.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignupData(
    @SerializedName("nickname")
    val nickname: String? = null,
    @SerializedName("loginId")
    val loginId: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("question")
    val question: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("answer")
    val answer: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("cfPassword")
    val cfPassword: String? = null,
    @SerializedName("kanaFirstName")
    val kanaFirstName: String? = null,
    @SerializedName("kanaLastName")
    val kanaLastName: String? = null,
    @SerializedName("hiraFirstName")
    val hiraFirstName: String? = null,
    @SerializedName("hiraLastName")
    val hiraLastName: String? = null,
    @SerializedName("mail1RecievFlg")
    val mail1RecievFlg: String? = null,
    @SerializedName("mail2RecievFlg")
    val mail2RecievFlg: String? = null,
    @SerializedName("mail1AdMailRecieveFlg")
    val mail1AdMailRecieveFlg: String? = null,
    @SerializedName("mail2AdMailRecieveFlg")
    val mail2AdMailRecieveFlg: String? = null,
): Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}