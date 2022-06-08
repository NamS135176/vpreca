package com.lifecard.vpreca.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class AuthToken(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("memberNumber")
    val memberNumber: String,
    @SerializedName("loginId")
    val loginId: String,
)

fun AuthToken.toJSON(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.toAuthToken(): AuthToken {
    val gson = Gson()
    return gson.fromJson(this, AuthToken::class.java)
}
