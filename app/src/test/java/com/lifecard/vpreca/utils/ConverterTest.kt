package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ConverterTest {
    @Test
    fun formatCardDateTimeMonth_isCorrect() {
        val strDate1 = "2022-05-27T10:00:00-0700"
        val strDate2 = "2022-12-27T10:00:00-0700"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date1: Date = dateFormat.parse(strDate1) as Date
        val date2: Date = dateFormat.parse(strDate2) as Date

        Assert.assertEquals("20225月27日", Converter.formatCardDateTimeMonth(date1))
        Assert.assertEquals("202212月27日", Converter.formatCardDateTimeMonth(date2))
        Assert.assertEquals("", Converter.formatCardDateTimeMonth(null))
    }

    @Test
    fun convertCardValidThu_isCorrect() {
        val strDate1 = "2022-05-27T10:00:00-0700"
        val strDate2 = "2022-12-27T10:00:00-0700"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date1: Date = dateFormat.parse(strDate1) as Date
        val date2: Date = dateFormat.parse(strDate2) as Date

        Assert.assertEquals("2022月5日", Converter.convertCardValidThu(date1))
        Assert.assertEquals("2022月12日", Converter.convertCardValidThu(date2))
    }

    @Test
    fun convertCardValidShort_isCorrect() {
        val strDate1 = "2022-05-05T10:00:00-0700"
        val strDate2 = "2022-12-27T10:00:00-0700"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date1: Date = dateFormat.parse(strDate1) as Date
        val date2: Date = dateFormat.parse(strDate2) as Date

        Assert.assertEquals("05/05", Converter.convertCardValidShort(date1))
        Assert.assertEquals("12/27", Converter.convertCardValidShort(date2))
    }

    @Test
    fun convertCurrency_isCorrect() {
        Assert.assertEquals("¥100", Converter.convertCurrency(100, "¥"))
        Assert.assertEquals("¥1,000", Converter.convertCurrency(1000, "¥"))
        Assert.assertEquals("¥10,000", Converter.convertCurrency(10000, "¥"))
        Assert.assertEquals("¥100,000", Converter.convertCurrency(100000, "¥"))
        Assert.assertEquals("¥1,000,000", Converter.convertCurrency(1000000, "¥"))

        Assert.assertEquals("¥100", Converter.convertCurrency("100"))
    }
}