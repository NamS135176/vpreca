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
    var description: String,
    @SerializedName("boundingPoly")
    val boundingPoly: VisionTextVertices
) {
    override fun toString(): String {
        return "VisionTextAnnotation (description: ${description})"
    }
}

data class VisionTextVertices(
    @SerializedName("vertices")
    val vertices: List<VisionTextLocation>
)

data class VisionTextLocation(
    @SerializedName("x")
    var x: Int,
    @SerializedName("y")
    val y: Int,
)