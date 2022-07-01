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
    val imageContext: VisionImageContext?,
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
    val languageHints: Array<String>?,
    @SerializedName("textDetectionParams")
    val textDetectionParams: VisionTextDetectionParams?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisionImageContext

        if (languageHints != null) {
            if (other.languageHints == null) return false
            if (!languageHints.contentEquals(other.languageHints)) return false
        } else if (other.languageHints != null) return false
        if (textDetectionParams != other.textDetectionParams) return false

        return true
    }

    override fun hashCode(): Int {
        var result = languageHints?.contentHashCode() ?: 0
        result = 31 * result + (textDetectionParams?.hashCode() ?: 0)
        return result
    }
}

data class VisionTextDetectionParams(
    @SerializedName("enableTextDetectionConfidenceScore")
    val enableTextDetectionConfidenceScore: Boolean
)