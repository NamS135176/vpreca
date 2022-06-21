package com.lifecard.vpreca.utils

import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer

fun RequestBody?.getMessageType(): String {
    return "unknown"
}

fun Request.bodyToString(): String? {
    val buffer = Buffer()
    try {
        val copy = this.newBuilder().build()
        copy.body()?.writeTo(buffer)
        return buffer.readUtf8()
    } catch (e: Exception) {
    } finally {
        buffer.close()
    }
    return null
}