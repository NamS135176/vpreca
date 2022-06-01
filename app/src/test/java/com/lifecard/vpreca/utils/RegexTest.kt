package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class RegexTest {
    @Test
    fun ocrCode_isCorrect() {
        val testData = "cdFjSst6Mb7rF29"
        Assert.assertEquals(true, RegexUtils.isOcrCode(testData))
    }

    @Test
    fun mobilePhone_isCorrect() {
        val phone1 = "070000000"
        val phone2 = "09000000000"
        val phone3 = "09000000000"
        Assert.assertEquals(true, RegexUtils.isMobilePhone(phone1))
        Assert.assertEquals(true, RegexUtils.isMobilePhone(phone2))
        Assert.assertEquals(true, RegexUtils.isMobilePhone(phone3))
    }

}