package com.lifecard.vpreca.ui.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.api.AWSTextractService
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.textract.TextractRequest
import com.lifecard.vpreca.data.textract.TextractResponse
import com.lifecard.vpreca.data.textract.TextractResponseBlock
import com.lifecard.vpreca.data.vision.*
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.encodeImage
import com.lifecard.vpreca.utils.getScaledDownBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@Suppress("kotlin:S1192") // String literals should not be duplicated
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val googleVisionService: GoogleVisionService,
    private val awsTextractService: AWSTextractService
) :
    ViewModel() {

    var codeOcr = MutableLiveData<String?>()
    var loading = MutableLiveData<Boolean?>()
    var error = MutableLiveData<String?>()
    var lockButtonTakePhoto = MutableLiveData<Boolean>(false)
    var networkTrouble = MutableLiveData<Boolean>()

    private fun getCodeByGoogleVisionOcr(
        context: Context,
        bitmap: Bitmap,
        percentTop: Float = 0f,
        percentHeight: Float = 1f,
        rotation: Int
    ) {
        viewModelScope.launch {
            var ocr: Result<String>? = null
            cropBitmap(bitmap, percentTop, percentHeight, rotation)?.let { cropped ->
                ocr = callApiGetOcr(context, cropped)
                cropped.recycle()
            }
            if (ocr == null || ocr is Result.Error) {
                val originBitmap = getBitmapFixExif(bitmap, rotation)
                ocr = callApiGetOcr(context, originBitmap, false)
                originBitmap.recycle()
            }

            bitmap.recycle()
            if (ocr is Result.Success) {
                val resultSuccess = (ocr as Result.Success<String>)
                codeOcr.value = resultSuccess.data
            } else if (ocr is Result.Error) {
                val resultError = ocr as Result.Error
                if (resultError.exception is NoConnectivityException) {
                    networkTrouble.value = true
                } else {
                    error.value = resultError.exception.message
                }
            }
            releaseLockTakePhoto()
            loading.value = false
        }
    }

    fun startTakePhoto() {
        loading.value = true
        lockTakePhoto()
    }

    fun lockTakePhoto() {
        lockButtonTakePhoto.value = true
    }
    fun releaseLockTakePhoto(hideLoading: Boolean = false) {
        if (hideLoading) loading.value = false
        lockButtonTakePhoto.value = false
    }

    private suspend fun callApiGetOcr(
        context: Context,
        bitmap: Bitmap,
        safeCheckSpecialChar: Boolean = true,
        defaultResultCode: String? = null
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val error = Result.Error(Exception("Can not detect ocr"))
                val scaleBitmap = bitmap.getScaledDownBitmap(1024, isNecessaryToKeepOrig = false)
                    ?: return@withContext error
//                val scaleBitmap = bitmap
                val imageBase64 = scaleBitmap.encodeImage() ?: return@withContext error
                val gcpApiKey = BuildConfig.GoogleApiKey

                val requestContent = VisionImageRequestContent(
                    image = VisionImageContent(content = imageBase64),
                    features = listOf(VisionFeature(type = "TEXT_DETECTION")),
                    imageContext = VisionImageContext(
                        languageHints = arrayOf("en-t-i0"),
                        textDetectionParams = VisionTextDetectionParams(
                            enableTextDetectionConfidenceScore = true
                        )
                    )
                )

                val request = VisionImageRequest(requests = listOf(requestContent))
                val result = googleVisionService.detectOcr(gcpApiKey, request)
                val textAnnotations = result.responses[0].textAnnotations
                val codeResult = findBestCodeFromData(textAnnotations = textAnnotations)

                if (codeResult != null) {
                    println("result of detectOcr: $codeResult")
                    if (safeCheckSpecialChar && codeResult.description.contains(Regex("[O]"))) {
                        //crop image with vertices
                        val padding = 15
                        val vertices = codeResult.boundingPoly.vertices
                        var x = min(vertices[0].x, vertices[1].x)
                        x = max(x - padding, 0)//for safe never minus

                        var y = min(vertices[0].y, vertices[1].y)
                        y = max(y - padding, 0)//for safe never minus

                        val targetX = max(vertices[2].x, vertices[1].x) + padding
                        val targetY = max(vertices[3].y, vertices[2].y) + padding

                        val width = targetX + padding - x
                        val height = targetY + padding - y
                        val bitmapBoundingText =
                            Bitmap.createBitmap(scaleBitmap, x, y, width, height)
//                        saveMediaToStorage(context, bitmapBoundingText)

                        return@withContext callApiGetOcr(
                            context,
                            bitmapBoundingText,
                            false,
                            codeResult.description
                        )
                    }
                    Result.Success(codeResult.description)
                } else if (defaultResultCode != null) {
                    Result.Success(defaultResultCode)
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

    fun ocrTextractDetect(
        context: Context,
        bitmap: Bitmap,
        rotation: Int,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ) {
        viewModelScope.launch {
            val originBitmap = getBitmapFixExif(bitmap, rotation)
            val scaleBitmap =
                originBitmap.getScaledDownBitmap(1024, isNecessaryToKeepOrig = false)
                    ?: return@launch
            val imageBase64 = scaleBitmap.encodeImage() ?: return@launch
            val result = apiOcrTextractDetect(imageBase64)
            var isTextractSuccess = false

            if (result is Result.Success) {
                val response = result.data
                val ocr = findBestCodeFromTextract(response.result.blocks)
                ocr?.let {
                    codeOcr.value = it
                    isTextractSuccess = true
                }
                    ?: kotlin.run {
                        getCodeByGoogleVisionOcr(
                            context,
                            bitmap,
                            percentTop,
                            percentHeight,
                            rotation
                        )
                    }
            } else if (result is Result.Error) {
                getCodeByGoogleVisionOcr(context, bitmap, percentTop, percentHeight, rotation)
            }
            if (isTextractSuccess) {
                bitmap.recycle()
                releaseLockTakePhoto()
                loading.value = false
            }
        }
    }

    private suspend fun apiOcrTextractDetect(imageBase64: String): Result<TextractResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    awsTextractService.ocrTextractDetect(TextractRequest(imageBase64))
                Result.Success(response)
            } catch (e: UnknownHostException) {
                println(e)
                Result.Error(NoConnectivityException())
            } catch (e: IOException) {
                println(e)
                Result.Error(NoConnectivityException())
            } catch (e: Exception) {
                Result.Error(Exception("Can not detect ocr"))
            }
        }
    }

    fun findBestCodeFromData(textAnnotations: List<VisionTextAnnotation>): VisionTextAnnotation? {
        try {
            //1. delete all ( + ) + : + spacing on text
            textAnnotations.forEach {
                it.description =
                    RegexUtils.replaceSpecialCaseOcrCode(it.description) ?: it.description
                it.description = it.description.trim().replace(Regex("[\\(\\)\\s:]"), "")
                //remove all not alphabet letter and number
                it.description = it.description.replace(Regex("[^A-z0-9]"), "")
            }
            //2. check the regex
            val texts = textAnnotations.filter { RegexUtils.isOcrCode(it.description) }
            return when (texts.isNotEmpty()) {
                true -> texts[0]
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun findBestCodeFromTextract(blocks: List<TextractResponseBlock>): String? {
        try {
            val filterBlocks = blocks.filter { !it.text.isNullOrEmpty() }
            //1. delete all ( + ) + : + spacing on text
            filterBlocks.forEach {
                it.text = RegexUtils.replaceSpecialCaseOcrCode(it.text)
                it.text = it.text?.trim()?.replace(Regex("[\\(\\)\\s:]"), "")
                //remove all not alphabet letter and number
                it.text = it.text?.replace(Regex("[^A-z0-9]"), "")
            }
            //2. check the regex ocr code
            val texts = filterBlocks.filter { RegexUtils.isOcrCode(it.text) }
            return when (texts.isNotEmpty()) {
                true -> texts[0].text
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getBitmapFixExif(
        bitmap: Bitmap,
        rotationInDegrees: Int
    ): Bitmap {
        var originBitmap = bitmap

        if (rotationInDegrees != 0) {
            val matrix = Matrix()
            matrix.preRotate(rotationInDegrees.toFloat())
            originBitmap = Bitmap.createBitmap(
                originBitmap,
                0,
                0,
                originBitmap.width,
                originBitmap.height,
                matrix,
                true
            )
        }
        return originBitmap
    }

    private fun cropBitmap(
        bitmap: Bitmap,
        percentTop: Float = 0f,
        percentHeight: Float = 1f,
        rotation: Int
    ): Bitmap? {
        try {
            val originBitmap = getBitmapFixExif(bitmap, rotation)

            val bitmapW = originBitmap.width
            val bitmapH = originBitmap.height
            val y = bitmapH * percentTop
            val height = bitmapH * percentHeight

            return Bitmap.createBitmap(
                originBitmap,
                0,
                y.toInt(),
                bitmapW,
                height.toInt(),
            )
        } catch (e: Exception) {
            println(e)
            return null
        }
    }
}