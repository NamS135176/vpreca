package com.lifecard.vpreca.utils

import org.junit.Assert
import org.junit.Test

class DeviceIDHelperTest {
    @Test
    fun generateDeviceID_isCorrect() {
        Assert.assertEquals(
            32,
            DeviceIDHelper.generateDeviceID().length
        )
    }
}