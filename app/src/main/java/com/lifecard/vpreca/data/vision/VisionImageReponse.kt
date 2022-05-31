package com.lifecard.vpreca.data.vision

import com.google.gson.annotations.SerializedName

data class VisionImageResponses(
    @SerializedName("responses")
    val responses: List<VisionImageResponse>
)


data class VisionImageResponse(
    @SerializedName("textAnnotations")
    val textAnnotations: List<VisionTextAnnotation>,
)

data class VisionTextAnnotation(
    @SerializedName("description")
    var description: String
) {
    override fun toString(): String {
        return "VisionTextAnnotation (description: ${description})"
    }
}