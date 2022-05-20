package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class BioChallenge(
    @SerializedName("salt")
    val salt: String,
    @SerializedName("challenge")
    val challenge: String,
    @SerializedName("nonce")
    val nonce: String
)