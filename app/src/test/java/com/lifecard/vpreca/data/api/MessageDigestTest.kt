package com.lifecard.vpreca.data.api

import com.lifecard.vpreca.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MessageDigestTest {
    private val messageDigest = MessageDigest()

    @Test
    fun testSign_isCorrect() {
        val textSign = "mytext"
        Assert.assertEquals("a7438c07a130e7439f4f3118d92ca2d6ddc91bd8bce1282d6b3945b4faf3e0ef4f5fe929ca43f9ce1d54944fbe89920317326607d833fb0b42bfb47d55a26813d203da71b0faa7a84940e6ca7b48267ac362548e62ef635b23528c75edb77db96a886d14728d79708282f5fac942a0f43299c433b87be929cb13af6765c1bbef2642fa700c9438623efa08249e286b2a031ff18a62c1cfb6693740a3061de55fa34d4736265770302e212e1158354f05e1f5c9446b2f0358292e1824948f01d5f5614d7950553901ef95e371d4757d98da9ec90a986be6af6c8346aa9806d26da24ae604c0ccdec626d079522dfa44509421f67364fd5665055c3f8e16a74bd1", messageDigest.sign(textSign))

        Assert.assertEquals(512, messageDigest.sign(Utils.randomString(10000))?.length)
    }
}