package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test
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
            "abc%^!#$@%@",
        )
        loginIdErrors.forEach { loginId ->
            Assert.assertEquals(
                false,
                RegexUtils.isLoginIdValid(loginId)
            )
        }
        Assert.assertEquals(true, RegexUtils.isLoginIdValid("-23_202020"))
    }

    @Test
    fun formatDisplayPhoneNumber_isCorrect() {
        Assert.assertEquals(
            "090-1234-5678",
            RegexUtils.formatDisplayPhoneNumber("09012345678")
        )
        Assert.assertEquals(
            "090-1234-567",
            RegexUtils.formatDisplayPhoneNumber("0901234567")
        )
        Assert.assertEquals(
            "090-1234-567",
            RegexUtils.formatDisplayPhoneNumber("090-1234-567")
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

    @Test
    fun isFullWidth_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isFullWidth("アキ・秋・あき・ａｋｉ・ＡＫＩ|ーＺｚ"))
        Assert.assertEquals(true, RegexUtils.isFullWidth("テアィン"))
        Assert.assertEquals(false, RegexUtils.isFullWidth("abc"))
    }

    @Test
    fun isAnswer_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isAnswerValid("アキ・秋・あき・ａｋｉ・ＡＫＩ|ーＺｚ"))
        Assert.assertEquals(true, RegexUtils.isAnswerValid("テアィン"))
        Assert.assertEquals(false, RegexUtils.isAnswerValid("テアィンテアィンテアィンテアィンテアィンテアィンテアィンテアィンテアィン"))
    }

    @Test
    fun isCreditCard_isCorrect() {
        //American Express
        Assert.assertEquals(true, RegexUtils.isCreditCard("3711-078176-01234"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("371107817601234"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("3711 078176 01234"))

        //Visa
        Assert.assertEquals(true, RegexUtils.isCreditCard("4123-5123-6123-7123"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("4123512361237123"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("4123 5123 6123 7123"))

        //Master Card
        Assert.assertEquals(true, RegexUtils.isCreditCard("5123-4123-6123-7123"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("5123412361237123"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("5123 4123 6123 7123"))

        //Discover
        Assert.assertEquals(true, RegexUtils.isCreditCard("6011-0009-9013-9424"))
        Assert.assertEquals(true, RegexUtils.isCreditCard("6500000000000002"))
        Assert.assertEquals(true, RegexUtils.isCreditCard(" 6011 0009 9013 9424"))
    }

    @Test
    fun isKatakanaFullWidth_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isKatakanaFullWidth("アキ"))

        Assert.assertEquals(false, RegexUtils.isKatakanaFullWidth("AKI"))
        Assert.assertEquals(false, RegexUtils.isKatakanaFullWidth("aki"))
        Assert.assertEquals(false, RegexUtils.isKatakanaFullWidth("ａｋｉ"))
        Assert.assertEquals(false, RegexUtils.isKatakanaFullWidth("ＡＫＩ"))
        Assert.assertEquals(false, RegexUtils.isKatakanaFullWidth("ｱｷ"))
    }

    @Test
    fun isNickname_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isNicknameValid("ABC"))
        Assert.assertEquals(true, RegexUtils.isNicknameValid("ABCABCABCABCABCABC"))

        Assert.assertEquals(false, RegexUtils.isNicknameValid("ABC123"))
        Assert.assertEquals(false, RegexUtils.isNicknameValid("ABCABCABCABCABCABCA"))
        Assert.assertEquals(false, RegexUtils.isNicknameValid("abc"))
        Assert.assertEquals(false, RegexUtils.isNicknameValid("アキ"))
    }
}