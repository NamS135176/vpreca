package com.lifecard.vpreca.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeInfoMemberData(
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("loginId")
    val loginId: String?,
    @SerializedName("memberRoman")
    val memberRoman: String?,
    @SerializedName("memberKana")
    val memberKana: String?,
    @SerializedName("memberName")
    val memberName: String?,
    @SerializedName("addressPrefecture")
    val addressPrefecture: String?,
    @SerializedName("mailAddress1")
    val mailAddress1: String?,
    @SerializedName("mailAddress2")
    val mailAddress2: String?,
    @SerializedName("secretQuestion")
    val secretQuestion: String?,
    @SerializedName("secretQuestionAnswer")
    val secretQuestionAnswer: String?,
    @SerializedName("mail1AdMailRecieveFlg")
    val mail1AdMailRecieveFlg: String?,
    @SerializedName("mail2AdMailRecieveFlg")
    val mail2AdMailRecieveFlg: String?,
    @SerializedName("mail1RecievFlg")
    val mail1RecievFlg: String?,
    @SerializedName("mail2RecievFlg")
    val mail2RecievFlg: String?,
    @SerializedName("telephoneNumber1")
    val telephoneNumber1: String?,
) : Parcelable {
    override fun hashCode(): Int {
        return super.hashCode()
    }
}

fun ChangeInfoMemberData.getFirstKanaName(): String? {
    return try {
        memberKana?.split(" ")?.get(0)
    } catch (e: Exception) {
        println(e)
        memberKana
    }
}

fun ChangeInfoMemberData.getLastKanaName(): String? {
    return try {
        memberKana?.split(" ")?.get(1)
    } catch (e: Exception) {
        println(e)
        ""
    }
}

fun ChangeInfoMemberData.getFirstMemberName(): String? {
    return try {
        memberName?.split(" ")?.get(0)
    } catch (e: Exception) {
        println(e)
        memberName
    }
}

fun ChangeInfoMemberData.getLastMemberName(): String? {
    return try {
        memberName?.split(" ")?.get(1)
    } catch (e: Exception) {
        println(e)
        ""
    }
}