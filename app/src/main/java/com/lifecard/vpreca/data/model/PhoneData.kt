package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneData(
    @SerializedName("phone")
    var phone: String,
    @SerializedName("memberNumber")
    var memberNumber: String,
    @SerializedName("loginId")
    var loginId: String,
    @SerializedName("memberRoman")
    var memberRoman: String,
    @SerializedName("memberKana")
    var memberKana: String,
    @SerializedName("memberName")
    var memberName: String,
    @SerializedName("addressCity")
    var addressCity: String,
    @SerializedName("mailAddress1")
    var mailAddress1: String,
    @SerializedName("mailAddress2")
    var mailAddress2: String,
    @SerializedName("secretQuestion")
    var secretQuestion: String,
    @SerializedName("secretQuestionAnswer")
    var secretQuestionAnswer: String,
    @SerializedName("mail1AdMailRecieveFlg")
    var mail1AdMailRecieveFlg: String,
    @SerializedName("mail2AdMailRecieveFlg")
    var mail2AdMailRecieveFlg: String,
    @SerializedName("mail1RecievFlg")
    var mail1RecievFlg: String,
    @SerializedName("mail2RecievFlg")
    var mail2RecievFlg: String,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}