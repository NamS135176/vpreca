package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ListDesignRequest(
    @SerializedName("cardSchemeId")
    val cardSchemeId: String,
)
