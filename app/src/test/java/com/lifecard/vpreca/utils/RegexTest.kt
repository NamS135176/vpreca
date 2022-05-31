package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class RegexTest {
    @Test
    fun convertDateTimeMonth_isCorrect() {
        var testData = "cdFjSst6Mb7rF29"
        Assert.assertEquals(true, RegexUtils.isOcrCode(testData))
    }

}