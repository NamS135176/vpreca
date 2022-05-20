package com.lifecard.vpreca.exception

import java.io.IOException

class ApiException(val statusCode: Int?) : IOException() {
    override val message: String
        get() = "ApiException (statusCode = $statusCode)"
}