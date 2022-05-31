package com.lifecard.vpreca.ui.ocr

import com.lifecard.vpreca.TestHelper
import com.lifecard.vpreca.data.api.ApiServiceFactory
import com.lifecard.vpreca.data.vision.VisionImageResponses
import org.junit.Assert
import org.junit.Test

class CameraViewModelTest {
    private val helper = TestHelper()

    @Test
    fun testOcrDetect1() {
        val jsonString = helper.loadJsonAsString("ocr1.json")
        Assert.assertNotNull(jsonString)

        val googleVisionResponse =
            helper.convertJsonToModel(jsonString, VisionImageResponses::class.java)
        Assert.assertNotNull(googleVisionResponse)

        val viewModel = CameraViewModel(ApiServiceFactory.createGoogleVisionService())
        val ocr = viewModel.findBestCodeFromData(googleVisionResponse.responses[0].textAnnotations)
        println("ocr... $ocr")
        Assert.assertEquals("cdFjSst6Mb7rF29", ocr)
    }
}