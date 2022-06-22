package com.lifecard.vpreca.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class AuthToken(
    @SerializedName("accessToken")
    var accessToken: String? = null,
    @SerializedName("refreshToken")
    var refreshToken: String? = null,
    @SerializedName("memberNumber")
    var memberNumber: String? = null,
    @SerializedName("loginId")
    var loginId: String? = null,
)

fun AuthToken.toJSON(): String? {
    try {
        val gson = Gson()
        return gson.toJson(this)
    } catch (e: Exception) {

    }
    return null
}

fun String.toAuthToken(): AuthToken? {
    try {
        val gson = Gson()
        return gson.fromJson(this, AuthToken::class.java)
    } catch (e: Exception) {

    }
    return null
}

fun AuthToken.isEmpty(): Boolean {
    return accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty() || memberNumber.isNullOrEmpty()
            || loginId.isNullOrEmpty()
}

fun AuthToken.clear() {
    accessToken = null
    refreshToken = null
    memberNumber = null
    loginId = null
}