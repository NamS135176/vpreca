package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.vision.VisionImageRequest
import com.lifecard.vpreca.data.vision.VisionImageResponses
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleVisionService {
    @POST("/v1/images:annotate")
    suspend fun detectOcr(
        @Query("key") gcpApiKey: String,
        @Body request: VisionImageRequest
    ): VisionImageResponses

}