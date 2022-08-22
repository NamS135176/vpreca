package com.lifecard.vpreca.utils

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


object Converter {
    @JvmStatic
    fun formatCardDateTimeMonth(date: Date?): String {
        return date?.let {
            val localDate = LocalDate.fromDateFields(date)

            val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy年M月d日")
            localDate.toString(fmt)
        } ?: ""
    }

    @JvmStatic
    fun convertCardValidThu(date: Date?): String {
        return date?.let {
            val localDate = LocalDate.fromDateFields(date)

            val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy月M日")
            localDate.toString(fmt)
        } ?: ""
    }

    @JvmStatic
    fun convertCardValidShort(date: Date?): String {
        return date?.let {
            val localDate = LocalDate.fromDateFields(date)

            val fmt: DateTimeFormatter = DateTimeFormat.forPattern("MM/YY")
            localDate.toString(fmt)
        } ?: ""

    }

    @JvmStatic
    fun convertCardValidShortString(date: String?): String {
        return date?.let {
            val myFormat = "yyyyMMdd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val data = sdf.parse(date)

            val localDate = LocalDate.fromDateFields(data)

            val fmt: DateTimeFormatter = DateTimeFormat.forPattern("MM/YY")
            localDate.toString(fmt)
        } ?: ""

    }

    @JvmStatic
    fun convertColor(text: String): Int {
        return Color.parseColor(text)
    }

    @JvmStatic
    fun convertCurrency(value: Int, defaultCurrency: String = "¥"): String {
       if(value <0) {
           val formatter: NumberFormat = DecimalFormat("#,###")
           return "-${defaultCurrency}${formatter.format(-value)}"
       }
        else{
           val formatter: NumberFormat = DecimalFormat("#,###")
           return "${defaultCurrency}${formatter.format(value)}"
       }
    }

    @JvmStatic
    fun convertLongText(text: String): String {
        var cvText = ""
        if (text.length > 10) {
            cvText = text.substring(0, 9) + "..."
        } else {
            cvText = text
        }
        return cvText
    }

    @JvmStatic
    fun convertCurrency(value: String?): String {
        return try {
            convertCurrency(value?.let { Integer.parseInt(it) } ?: 0)
        } catch (err: Exception) {
            convertCurrency(0)
        }
    }

    @JvmStatic
    fun convertPrecaNumber(value: String?): String {
        val show = value?.substring(value.length.minus(5), value.length)
        return "**** **** **** " + show
    }

    @JvmStatic
    fun convertRemain(total: String?, gift: String?): String {
        val totalInt = total?.toInt()
        val giftInt = gift?.toInt()
        val remain = totalInt!! - giftInt!!
        if (remain > 0) {
            return remain.toString()
        } else {
            return "0"
        }
    }

    @JvmStatic
    fun isDateValid(date: String?, pattern: String? = "yyyy年MM月dd日"): Boolean {
        return try {
            date?.let { SimpleDateFormat(pattern, Locale.JAPAN).parse(date).before(Date()) }
                ?: false
        } catch (e: java.lang.Exception) {
            false
        }
    }

    @JvmStatic
    fun convertBooleanToString(state: Boolean): String {
        if (state) {
            return "1"
        } else return "0"
    }

    @JvmStatic
    fun convertStringToBoolean(state: String): Boolean {
        return state == "1"
    }

    @JvmStatic
    fun formatToolbarTitle(title: String, max: Int = 12): String {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                title
            } else {
                title.substring(0, min(max, title.length))
            }
        } catch (e: Exception) {
            title
        }
    }

    @JvmStatic
    fun formatToolbarTitleToNull(title: String, max: Int = 12): String {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                title
            } else {
                title.substring(0, min(max, title.length))
            }
        } catch (e: Exception) {
            ""
        }
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    @JvmStatic
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    @JvmStatic
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}