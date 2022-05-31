package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class MemberInfo(

    @SerializedName("bgOneClickDbKey")
    val bgOneClickDbKey: String?,
    @SerializedName("creditBrandName")
    val creditBrandName: String?,
    @SerializedName("creditCardExpirationDate")
    val creditCardExpirationDate: String?,
    @SerializedName("creditCardKind")
    val creditCardKind: String?,
    @SerializedName("creditCardNumber4Digit")
    val creditCardNumber4Digit: String?,
    @SerializedName("addressCity")
    val addressCity: String?,
    @SerializedName("addressHouse")
    val addressHouse: String?,
    @SerializedName("addressKana")
    val addressKana: String?,
    @SerializedName("addressLot")
    val addressLot: String?,
    @SerializedName("addressPrefecture")
    val addressPrefecture: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("blackListFlg")
    val blackListFlg: String?,
    @SerializedName("blackListStatusUpdateDate")
    val blackListStatusUpdateDate: String?,
    @SerializedName("blackListType")
    val blackListType: String?,
    @SerializedName("confirmApplyDate")
    val confirmApplyDate: String?,
    @SerializedName("confirmDate")
    val confirmDate: String?,
    @SerializedName("confirmDealAim")
    val confirmDealAim: String?,
    @SerializedName("confirmDocumentFlg")
    val confirmDocumentFlg: String?,
    @SerializedName("confirmDocumentNumber")
    val confirmDocumentNumber: String?,
    @SerializedName("confirmDocumentSendSDate")
    val confirmDocumentSendSDate: String?,
    @SerializedName("confirmFlg")
    val confirmFlg: String?,
    @SerializedName("confirmJobType")
    val confirmJobType: String?,
    @SerializedName("confirmType")
    val confirmType: String?,
    @SerializedName("creditCardEntryLockFlg")
    val creditCardEntryLockFlg: String?,
    @SerializedName("giftEntryLockFlg")
    val giftEntryLockFlg: String?,
    @SerializedName("lastChangePasswordDate")
    val lastChangePasswordDate: String?,
    @SerializedName("lastLoginDate")
    val lastLoginDate: String?,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("mail1AdMailRecieveFlg")
    val mail1AdMailRecieveFlg: String?,
    @SerializedName("mail1LostFlg")
    val mail1LostFlg: String?,
    @SerializedName("mail1RecievFlg")
    val mail1RecievFlg: String?,
    @SerializedName("mail2AdMailRecieveFlg")
    val mail2AdMailRecieveFlg: String?,
    @SerializedName("mail2LostFlg")
    val mail2LostFlg: String?,
    @SerializedName("mail2RecievFlg")
    val mail2RecievFlg: String?,
    @SerializedName("mailAddress1")
    val mailAddress1: String?,
    @SerializedName("mailAddress2")
    val mailAddress2: String?,
    @SerializedName("memberConfirmStat")
    val memberConfirmStat: String?,
    @SerializedName("memberKana")
    val memberKana: String?,
    @SerializedName("memberName")
    val memberName: String?,
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("memberRoman")
    val memberRoman: String?,
    @SerializedName("memberStatusFlg")
    val memberStatusFlg: String?,
    @SerializedName("postalCode")
    val postalCode: String?,
    @SerializedName("precaEntryLockFlg")
    val precaEntryLockFlg: String?,
    @SerializedName("registedDate")
    val registedDate: String?,
    @SerializedName("secretQuestion")
    val secretQuestion: String?,
    @SerializedName("secretQuestionAnswer")
    val secretQuestionAnswer: String?,
    @SerializedName("sex")
    val sex: String?,
    @SerializedName("suspendLock")
    val suspendLock: String?,
    @SerializedName("telephoneNumber1")
    val telephoneNumber1: String?,
    @SerializedName("telephoneNumber2")
    val telephoneNumber2: String?
)