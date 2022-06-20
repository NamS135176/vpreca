package com.lifecard.vpreca.ui.ocr

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.vision.*
import com.lifecard.vpreca.utils.getScaledDownBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.encodeImage
import java.io.IOException
import java.net.UnknownHostException

@HiltViewModel
class CameraViewModel @Inject constructor(private val googleVisionService: GoogleVisionService) :
    ViewModel() {

    var codeOcr = MutableLiveData<String?>()
    var loading = MutableLiveData<Boolean?>()
    var error = MutableLiveData<String?>()
    var lockButtonTakePhoto = MutableLiveData<Boolean>(false)
    var networkTrouble = MutableLiveData<Boolean>()

    fun getCodeByGoogleVisionOcr(bitmap: Bitmap) {
        viewModelScope.launch {
            loading.value = true
            val ocr = callApiGetOcr(bitmap)
            releaseLockTakePhoto()
            if (ocr is Result.Success) {
                codeOcr.value = ocr.data
            } else if (ocr is Result.Error) {
                if (ocr.exception is NoConnectivityException) {
                    networkTrouble.value = true
                } else {
                    error.value = ocr.exception.message
                }
            }
            loading.value = false
        }
    }

    fun startTakePhoto() {
        lockButtonTakePhoto.value = true
    }

    fun releaseLockTakePhoto() {
        lockButtonTakePhoto.value = false
    }

    private suspend fun callApiGetOcr(bitmap: Bitmap): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val error = Result.Error(Exception("Can not detect ocr"))
                val scaleBitmap = bitmap.getScaledDownBitmap(1024, isNecessaryToKeepOrig = true)
                    ?: return@withContext error
                val imageBase64 = scaleBitmap.encodeImage() ?: return@withContext error
                val gcpApiKey = BuildConfig.GoogleApiKey

                val requestContent = VisionImageRequestContent(
                    image = VisionImageContent(content = imageBase64),
                    features = listOf(VisionFeature(type = "TEXT_DETECTION")),
                    imageContext = VisionImageContext(
                        languageHints = "en",
                        textDetectionParams = VisionTextDetectionParams(
                            enableTextDetectionConfidenceScore = true
                        )
                    )
                )

                val request = VisionImageRequest(requests = listOf(requestContent))
                val result = googleVisionService.detectOcr(gcpApiKey, request)
                val textAnnotations = result.responses[0].textAnnotations
                println("result of detectOcr textAnnotations = $textAnnotations")
                val codeResult = findBestCodeFromData(textAnnotations = textAnnotations)
                if (!codeResult.isNullOrEmpty()) {
                    println("result of detectOcr: $codeResult")
                    Result.Success(codeResult!!)
                } else {
                    Result.Error(Exception("Can not detect ocr"))
                }
            } catch (e: UnknownHostException) {
                println(e)
                Result.Error(NoConnectivityException())
            } catch (e: IOException) {
                println(e)
                Result.Error(NoConnectivityException())
            } catch (e: Exception) {
                println("CameraViewModel...getCodeByGoogleVisionOcr has error $e")
                Result.Error(Exception("Can not detect ocr"))
            }
        }
    }

    fun findBestCodeFromData(textAnnotations: List<VisionTextAnnotation>): String? {
        try {
            //1. delete all ( or ) on text
            textAnnotations.forEach {
                it.description = it.description.trim().replace(Regex("[\\(\\)]"), "")
            }
            println("findBestCodeFromData... textAnnotations: $textAnnotations")
            //2. check the regex and length of text in (12..16)
            val texts = textAnnotations.filter { RegexUtils.isOcrCode(it.description) }
            println("findBestCodeFromData... texts: $texts")
            val results = listOf(15, 16, 14, 13, 12).mapNotNull {
                findBestCodeFromTextAnnotation(
                    texts,
                    it
                )
            }
            return when (results.isNotEmpty()) {
                true -> results[0]
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun findBestCodeFromTextAnnotation(
        textAnnotations: List<VisionTextAnnotation>,
        length: Int
    ): String? {
        try {
            val results = textAnnotations.filter { it.description.length == length }
            if (results.isNotEmpty()) return results[0].description
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}