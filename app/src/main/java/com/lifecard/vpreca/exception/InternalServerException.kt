package com.lifecard.vpreca.exception

import java.io.IOException

class InternalServerException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "No Internet Connection"
    // You can send any message whatever you want from here.
}