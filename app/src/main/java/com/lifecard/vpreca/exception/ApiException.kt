package com.lifecard.vpreca.exception

import java.io.IOException

class ApiException(val statusCode: Int?) : IOException() {
}