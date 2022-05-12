package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("action")
    val action: LoginAction,
)

enum class LoginAction(val value:String) {
    None("none"),
    SmsVerify("sms_verify"),
    UpdateAccount("update_account"),
}