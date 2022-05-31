package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class MemberSubInfo(
    @SerializedName("memberSubInfoNumber")
    val memberSubInfoNumber: String?,
    @SerializedName("memberName")
    val memberName: String?,
    @SerializedName("memberKana")
    val memberKana: String?,
    @SerializedName("memberRoman")
    val memberRoman: String?,
    @SerializedName("sex")
    val sex: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("postalCode")
    val postalCode: String?,
    @SerializedName("addressPrefecture")
    val addressPrefecture: String?,
    @SerializedName("addressCity")
    val addressCity: String?,
    @SerializedName("addressLot")
    val addressLot: String?,
    @SerializedName("addressHouse")
    val addressHouse: String?,
    @SerializedName("addressKana")
    val addressKana: String?,
    @SerializedName("telephoneNumber1")
    val telephoneNumber1: String?,
    @SerializedName("telephoneNumber2")
    val telephoneNumber2: String?,
    @SerializedName("mailAddress1")
    val mailAddress1: String?,
    @SerializedName("mailAddress2")
    val mailAddress2: String?,
    @SerializedName("mail1AdMailRecieveFlg")
    val mail1AdMailRecieveFlg: String?,
    @SerializedName("mail2AdMailRecieveFlg")
    val mail2AdMailRecieveFlg: String?,
    @SerializedName("creditCardInfo")
    val creditCardInfo: String?,
    @SerializedName("bgOneClickDbKey")
    val bgOneClickDbKey: String?,
    @SerializedName("creditCardNumber4Digit")
    val creditCardNumber4Digit: String?,
    @SerializedName("creditCardExpirationDate")
    val creditCardExpirationDate: String?,
    @SerializedName("creditCardKind")
    val creditCardKind: String?,
    @SerializedName("creditBrandName")
    val creditBrandName: String?,
)