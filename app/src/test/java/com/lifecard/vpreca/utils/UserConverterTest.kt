package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test

class UserConverterTest {
    @Test
    fun hideEmailAddress_isCorrect() {
        Assert.assertEquals("anh***@gmail.com", UserConverter.hideEmail("anhndt@gmail.com"))
        Assert.assertEquals("anh***@vn-sis.com", UserConverter.hideEmail("anhndt@vn-sis.com"))
        Assert.assertEquals("an@vn-sis.com", UserConverter.hideEmail("an@vn-sis.com"))
        Assert.assertEquals("anh***@vn-sis.com", UserConverter.hideEmail("anhdt-test123@vn-sis.com"))
    }

    @Test
    fun hidePhone_isCorrect() {
        Assert.assertEquals("******0690", UserConverter.hidePhone("0946670690"))
    }
    @Test
    fun formatPhone_isCorrect() {
        Assert.assertEquals("123-456-7890", UserConverter.formatPhone("1234567890"))
    }

    @Test
    fun hideCreditCard_isCorrect() {
        Assert.assertEquals("**** **** **** 9424", UserConverter.hideCreditCard("6011 0009 9013 9424"))
        Assert.assertEquals("****-****-****-9424", UserConverter.hideCreditCard("6011-0009-9013-9424"))
        Assert.assertEquals("************0002", UserConverter.hideCreditCard("6500000000000002"))
    }
}