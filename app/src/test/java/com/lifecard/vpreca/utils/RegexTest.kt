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
    fun loginId_isCorrect() {
        Assert.assertEquals(false, RegexUtils.isLoginIdValid(""))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("ＡＡＡ"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("ＡＡＡＡＡＡ"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("12345"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("12345678901"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("12 345678"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("12344\uD83D\uDE00333"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("abc%^!#\$@%@"))
        Assert.assertEquals(false, RegexUtils.isLoginIdValid("マチヤタナヤ"))

        Assert.assertEquals(true, RegexUtils.isLoginIdValid("-23_202020"))
        Assert.assertEquals(true, RegexUtils.isLoginIdValid("anhndt"))
    }

    @Test
    fun formatDisplayPhoneNumber_isCorrect() {
        Assert.assertEquals("090-1234-5678", RegexUtils.formatDisplayPhoneNumber("09012345678"))
        Assert.assertEquals("090-123-4567", RegexUtils.formatDisplayPhoneNumber("0901234567"))
        Assert.assertEquals("090-123-4567", RegexUtils.formatDisplayPhoneNumber("090-123-4567"))
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
        Assert.assertEquals(true, RegexUtils.isFullWidth("ａａａａ"))
        Assert.assertEquals(false, RegexUtils.isFullWidth("az"))
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
        Assert.assertEquals(true, RegexUtils.isNicknameValid("ABCABCABCABCABCABCA"))//max 19

        Assert.assertEquals(false, RegexUtils.isNicknameValid("ABC123"))
        Assert.assertEquals(false, RegexUtils.isNicknameValid("ABCABCABCABCABCABCAB"))//20 chars
        Assert.assertEquals(false, RegexUtils.isNicknameValid("abc"))
        Assert.assertEquals(false, RegexUtils.isNicknameValid("アキ"))
    }

    @Test
    fun hideEmailAddress_isCorrect() {
        Assert.assertEquals("anh***@gmail.com", RegexUtils.hideEmail("anhndt@gmail.com"))
        Assert.assertEquals("anh**D*@vn-sis.com", RegexUtils.hideEmail("anhndt@vn-sis.com"))
        Assert.assertEquals("an@vn-sis.com", RegexUtils.hideEmail("an@vn-sis.com"))
        Assert.assertEquals("anh***@vn-sis.com", RegexUtils.hideEmail("anhdt-test123@vn-sis.com"))
    }

    @Test
    fun hidePhone_isCorrect() {
        Assert.assertEquals("******0690", RegexUtils.hidePhone("0946670690"))
    }

    @Test
    fun hideCreditCard_isCorrect() {
        Assert.assertEquals("**** **** **** 9424", RegexUtils.hideCreditCard("6011 0009 9013 9424"))
        Assert.assertEquals("****-****-****-9424", RegexUtils.hideCreditCard("6011-0009-9013-9424"))
        Assert.assertEquals("************0002", RegexUtils.hideCreditCard("6500000000000002"))
    }

    @Test
    fun formatHideDisplayPhoneNumber_isCorrect() {
        Assert.assertEquals("***-****-5678", RegexUtils.formatHideDisplayPhoneNumber("09012345678"))
        Assert.assertEquals("***-***-4567", RegexUtils.formatHideDisplayPhoneNumber("0901234567"))
//        Assert.assertEquals("***-***-4567", RegexUtils.formatHideDisplayPhoneNumber("090-123-4567"))
    }

    @Test
    fun emailValid_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isEmailValid("email@example.com"))
        Assert.assertEquals(true, RegexUtils.isEmailValid("email@123.123.123.123"))
        Assert.assertEquals(true, RegexUtils.isEmailValid("emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemai@example.com"))//256 chars max

        Assert.assertEquals(false, RegexUtils.isEmailValid("ﾁﾉﾆ@bc.com"))
        Assert.assertEquals(false, RegexUtils.isEmailValid("ちち@bc.com"))
        Assert.assertEquals(false, RegexUtils.isEmailValid("emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemaia@example.com"))//257 failure
        Assert.assertEquals(false, RegexUtils.isEmailValid("a bc@vn.com"))
        Assert.assertEquals(false, RegexUtils.isEmailValid(null))
    }

    @Test
    fun isKanaNameFullWidth_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isKanaNameFullWidth("ヤマダ　アキ"))
        Assert.assertEquals(true, RegexUtils.isKanaNameFullWidth("ヤヤマダ　アキヤマダヤマダヤマダヤマダ"))//max19 chars
        Assert.assertEquals(false, RegexUtils.isKanaNameFullWidth("ヤヤヤマダ　アキヤマダヤマダヤマダヤマダ"))//20 chars failure
    }

    @Test
    fun isNameFullWidth_isCorrect() {
        Assert.assertEquals(true, RegexUtils.isNameFullWidth("山田　秋"))
        Assert.assertEquals(true, RegexUtils.isNameFullWidth("山田山田　秋秋秋秋秋秋秋秋秋秋秋秋秋秋"))//max19 chars
        Assert.assertEquals(false, RegexUtils.isNameFullWidth("山田山田　秋秋秋秋秋秋秋秋秋秋秋秋秋秋秋"))//20 chars failure
    }
}