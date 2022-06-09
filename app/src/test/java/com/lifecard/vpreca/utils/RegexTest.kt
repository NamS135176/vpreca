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

    @Test
    fun password_isInCorrect() {
        val passErrors =
            listOf("", "ＡＡＡ", "1234567", "1234567890123", "12 345678", "12344\uD83D\uDE00333")
        passErrors.forEach { pass -> Assert.assertEquals(false, RegexUtils.isPasswordValid(pass)) }
    }

    @Test
    fun loginId_isInCorrect() {
        val loginIdErrors = listOf(
            "",
            "ＡＡＡ",
            "ＡＡＡＡＡＡ",
            "12345",
            "12345678901",
            "12 345678",
            "12344\uD83D\uDE00333",
            "abc%^!#$@%@"
        )
        loginIdErrors.forEach { loginId ->
            Assert.assertEquals(
                false,
                RegexUtils.isLoginIdValid(loginId)
            )
        }
    }

    @Test
    fun formatDisplayPhoneNumber_isCorrect() {
        Assert.assertEquals(
            "090-123-45678",
            RegexUtils.formatDisplayPhoneNumber("09012345678")
        )
        Assert.assertEquals(
            "090-123-4567",
            RegexUtils.formatDisplayPhoneNumber("0901234567")
        )
        Assert.assertEquals(
            "090-123-4567",
            RegexUtils.formatDisplayPhoneNumber("090-123-4567")
        )
    }

    @Test
    fun checkLoginIdContainSpecialCharacter_isCorrect() {
        Assert.assertEquals(
            true,
            RegexUtils.checkLoginIdContainSpecialCharacter("!@#$%%^^&&**")
        )
        Assert.assertEquals(
            false,
            RegexUtils.checkLoginIdContainSpecialCharacter("-_")
        )
    }


}