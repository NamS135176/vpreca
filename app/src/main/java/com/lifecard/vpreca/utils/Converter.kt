package com.lifecard.vpreca.utils

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Converter {
    @JvmStatic
    fun convertDateTimeMonth(date: Date): String {
        val localDate = LocalDate.fromDateFields(date)

        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy月M日")
        return localDate.toString(fmt)
    }

    @JvmStatic
    fun convertCardValidThu(date: Date): String {
        val localDate = LocalDate.fromDateFields(date)

        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy月M日")
        return localDate.toString(fmt)
    }

    @JvmStatic
    fun convertCurrency(value: Int, defaultCurrency: String = "¥"): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return "${defaultCurrency}${formatter.format(value)}"
    }

    @JvmStatic
    fun convertCurrency(value: String?): String {
        return try {
            convertCurrency(value?.let { Integer.parseInt(it) } ?: 0)
        } catch (err: Exception) {
            convertCurrency(0)
        }
    }
}