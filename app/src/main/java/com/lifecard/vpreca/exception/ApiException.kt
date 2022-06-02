package com.lifecard.vpreca.exception

import com.lifecard.vpreca.utils.ApiError
import java.io.IOException

class ApiException(
    val resultCode: String,
    val messageType: String,
    private val errorMessage: String
) : IOException() {
    companion object {
        fun createApiException(
            resultCode: String,
            messageType: String,
        ): ApiException {
            return ApiException(
                resultCode,
                messageType,
                errorMessage = ApiError.getErrorMessage(messageType, resultCode = resultCode)
            )
        }
    }

    override val message: String
        get() = errorMessage
}
