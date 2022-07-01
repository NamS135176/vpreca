package com.lifecard.vpreca.data.textract

import com.google.gson.annotations.SerializedName


data class TextractRequest(
    @SerializedName("base64")
    val imageBase64: String
)
