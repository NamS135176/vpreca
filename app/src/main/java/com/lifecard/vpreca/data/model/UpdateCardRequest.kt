package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class UpdateCardRequest(
    @SerializedName("memberInfo")
    val memberInfo: MemberInfoContent,
    @SerializedName("cardInfo")
    val cardInfo: CardInfoRequestContentInfo,
    @SerializedName("changeInfo")
    val changeInfo: UpdateCardRequestContentInfo,
    @SerializedName("securityLock")
    val securityLock: SecurityLockContent,
)


data class UpdateCardRequestContentInfo(
    @SerializedName("nickName")
    val nickName: NickNameContent,
    @SerializedName("autoCharge")
    val autoCharge: AutoChargeContent,
)

data class NickNameContent(
    @SerializedName("cardNickname")
    val cardNickname: String,
)

data class AutoChargeContent(
    @SerializedName("autoChargeFlg")
    val autoChargeFlg: String,
    @SerializedName("autoChargeThereshold")
    val autoChargeThereshold: String,
    @SerializedName("autoChargeAmount")
    val autoChargeAmount: String,
)

data class SecurityLockContent(
    @SerializedName("vcnSecurityLockFlg")
    val vcnSecurityLockFlg: String?,
)
