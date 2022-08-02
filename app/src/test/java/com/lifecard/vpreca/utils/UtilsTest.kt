package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URI

@RunWith(RobolectricTestRunner::class)
class UtilsTest {
    @Test
    fun getMessageTypeFromUrl_isCorrect() {
        Assert.assertEquals(
            "LoginReq",
            Utils.getMessageTypeFromUrl(URI("https://xvojcilpig.execute-api.ap-southeast-1.amazonaws.com/staging/LoginReq"))
        )
    }
}