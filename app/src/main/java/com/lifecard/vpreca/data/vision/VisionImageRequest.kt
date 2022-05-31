package com.lifecard.vpreca.data.vision

import com.google.gson.annotations.SerializedName

data class VisionImageRequest(
    @SerializedName("requests")
    val requests: List<VisionImageRequestContent>,
)

data class VisionImageRequestContent(
    @SerializedName("image")
    val image: VisionImageContent,
    @SerializedName("features")
    val features: List<VisionFeature>,
    @SerializedName("imageContext")
    val imageContext: VisionImageContext,
)

data class VisionImageContent(
    @SerializedName("content")
    val content: String
)

data class VisionFeature(
    @SerializedName("type")
    val type: String
)

data class VisionImageContext(
    @SerializedName("languageHints")
    val languageHints: String,
    @SerializedName("textDetectionParams")
    val textDetectionParams: VisionTextDetectionParams,
)

data class VisionTextDetectionParams(
    @SerializedName("enableTextDetectionConfidenceScore")
    val enableTextDetectionConfidenceScore: Boolean
)