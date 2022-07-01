package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.data.textract.TextractRequest
import com.lifecard.vpreca.data.textract.TextractResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AWSTextractService {
    @POST("ocr-textract")
    suspend fun ocrTextractDetect(
        @Body request: TextractRequest
    ): TextractResponse
}