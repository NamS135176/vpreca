package com.lifecard.vpreca.data.model

import com.google.gson.annotations.SerializedName

data class ListDesignResponse(
    @SerializedName("brandPrecaApi")
    val brandPrecaApi: ListDesignBrandResponse,
)

data class ListDesignBrandResponse(
    @SerializedName("response")
    val response: ListDesignResponseContent,
)

data class ListDesignResponseContent(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("recordCount")
    val recordCount: Int?,
    @SerializedName("cardDesignInfo")
    val cardDesignInfo: List<CardDesignData>?,
)

