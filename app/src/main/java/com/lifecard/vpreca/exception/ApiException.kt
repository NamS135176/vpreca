package com.lifecard.vpreca.exception

import java.io.IOException

//class ApiException(val statusCode: Int?) : IOException() {
//    override val message: String
//        get() = "ApiException (statusCode = $statusCode)"
//}

class ApiException(
    val resultCode: Int,
    val messageType: String,
    private val errorMessage: String
) : IOException() {
    override val message: String
        get() = errorMessage
}