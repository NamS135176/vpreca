package com.lifecard.vpreca.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.requestDate(): String {
    val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.JAPAN)
    return formatter.format(this)
}