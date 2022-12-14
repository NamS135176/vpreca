package com.lifecard.vpreca.exception

import java.io.IOException

class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "Internal server error"

    // You can send any message whatever you want from here.
    val html: String? = null
}