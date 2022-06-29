package com.lifecard.vpreca.ui.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.vision.*
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.encodeImage
import com.lifecard.vpreca.utils.getScaledDownBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume

@Suppress("kotlin:S1192") // String literals should not be duplicated
@HiltViewModel
class CameraViewModel @Inject constructor(private val googleVisionService: GoogleVisionService) :
    ViewModel() {

    var codeOcr = MutableLiveData<String?>()
    var loading = MutableLiveData<Boolean?>()
    var error = MutableLiveData<String?>()
    var lockButtonTakePhoto = MutableLiveData<Boolean>(false)
    var networkTrouble = MutableLiveData<Boolean>()

    private val recognizer: TextRecognizer by lazy {
        TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
    }

    fun getCodeByGoogleVisionOcr(
        context: Context,
        imageUri: Uri,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ) {
        viewModelScope.launch {
            loading.value = true

            var ocr: Result<String>? = null
            cropBitmap(context, imageUri, percentTop, percentHeight)?.let { cropped ->
                /*
                ocr = mlKitDetectOcr(cropped)
                if (ocr == null || ocr is Result.Error) {
                    ocr = callApiGetOcr(cropped)
                }
                 */
                ocr = callApiGetOcr(context, cropped)
                cropped.recycle()
            }
            if (ocr == null || ocr is Result.Error) {
                val originBitmap = getBitmapFixExif(context, imageUri)
                /*
                ocr = mlKitDetectOcr(originBitmap)
                if (ocr == null || ocr is Result.Error) {
                    ocr = callApiGetOcr(originBitmap)
                }
                 */
                ocr = callApiGetOcr(context, originBitmap, false)
                originBitmap.recycle()
            }

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
            context.contentResolver.delete(imageUri, null, null)
            releaseLockTakePhoto()
            loading.value = false
        }
    }

    fun getCodeByGoogleVisionOcrByBitmap(
        context: Context,
        bitmap: Bitmap,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ) {
        viewModelScope.launch {
            loading.value = true

            val bitmapW = bitmap.width
            val bitmapH = bitmap.height
            val y = bitmapH * percentTop
            val height = bitmapH * percentHeight

            val cropBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                y.toInt(),
                bitmapW,
                height.toInt()
            )

            var ocr: Result<String>? = null
            cropBitmap?.let { cropped ->
                ocr = callApiGetOcr(context, cropped)
            }
            if (ocr == null || ocr is Result.Error) {
                ocr = callApiGetOcr(context, bitmap)
            }

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
        lockButtonTakePhoto.value = true
    }

    fun releaseLockTakePhoto() {
        lockButtonTakePhoto.value = false
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun mlKitDetectOcr(bitmap: Bitmap): Result<String> {
        return suspendCancellableCoroutine { cont ->
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val code = findBestCodeFromTextBlocksMLKit(visionText.textBlocks)
//                    println("recognizer.process... ${visionText.textBlocks}")
//                    visionText.textBlocks.forEach { println("recognizer.process... block ${it.text}") }
                    code?.let { cont.resume(Result.Success(it)) } ?: cont.resume(
                        Result.Error(
                            Exception("Can not detect ocr")
                        )
                    )
                }
                .addOnFailureListener { e ->
                    cont.resume(Result.Error(Exception("Can not detect ocr")))
                }
        }

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
                val scaleBitmap = bitmap.getScaledDownBitmap(1024, isNecessaryToKeepOrig = true)
                    ?: return@withContext error
                val imageBase64 = scaleBitmap.encodeImage() ?: return@withContext error
                val gcpApiKey = BuildConfig.GoogleApiKey

                val requestContent = VisionImageRequestContent(
                    image = VisionImageContent(content = imageBase64),
                    features = listOf(VisionFeature(type = "TEXT_DETECTION")),
                    imageContext = VisionImageContext(
                        languageHints = "ja",
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
                    if (safeCheckSpecialChar && codeResult.description.contains(Regex("[0||O]"))) {
                        //crop image with vertices
                        val vertices = codeResult.boundingPoly.vertices
                        val x = vertices[0].x
                        val y = vertices[0].y
                        val width = vertices[1].x - x
                        val height = vertices[2].y - y
                        val bitmapBoundingText =
                            Bitmap.createBitmap(scaleBitmap, x, y, width, height)
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

    fun findBestCodeFromData(textAnnotations: List<VisionTextAnnotation>): VisionTextAnnotation? {
        try {
            //1. delete all ( + ) + : + spacing on text
            textAnnotations.forEach {
                it.description = it.description.trim().replace(Regex("[\\(\\)\\s:]"), "")
                //remove all not alphabet letter and number
                it.description = it.description.replace(Regex("[^A-z0-9]"), "")
            }
            //2. check the regex and length of text in (12..16)
            val texts = textAnnotations.filter { RegexUtils.isOcrCode(it.description) }
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
    ): VisionTextAnnotation? {
        try {
            val results = textAnnotations.filter { it.description.length == length }
            if (results.isNotEmpty()) return results[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun findBestCodeFromTextBlocksMLKit(textBlocks: List<Text.TextBlock>): String? {
        try {
            //1. delete all ( + ) + : + spacing on text
            var texts = textBlocks.map {
                var text = it.text
                text = text.trim().replace(Regex("[\\(\\)\\s:]"), "")
                //remove all not alphabet letter and number
                text = text.replace(Regex("[^A-z0-9]"), "")
                text
            }
            //2. check the regex and length of text in (12..16)
            texts = texts.filter { RegexUtils.isOcrCodeOnly15Char(it) }
            return when (texts.isNotEmpty()) {
                true -> texts[0]
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getBitmapFixExif(
        context: Context,
        imageUri: Uri
    ): Bitmap {
        val rotationInDegrees = getRotationInDegrees(context, imageUri)
        val bitmapStream = context.contentResolver.openInputStream(imageUri)
        var originBitmap = BitmapFactory.decodeStream(bitmapStream)

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
        context: Context,
        imageUri: Uri,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ): Bitmap? {
        try {
            val originBitmap = getBitmapFixExif(context, imageUri)

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

    private fun getRotationInDegrees(context: Context, uri: Uri): Int {
        val exif = getExifInterface(context, uri)
        val rotation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        ) ?: ExifInterface.ORIENTATION_NORMAL

        return exifToDegrees(rotation)
    }

    private fun getExifInterface(context: Context, uri: Uri): ExifInterface? {
        try {
            val path = uri.toString()
            if (path.startsWith("file://")) {
                return ExifInterface(path)
            }
            if (path.startsWith("content://")) {
                val inputStream = context.contentResolver.openInputStream(uri)
                return inputStream?.let { ExifInterface(it) }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun exifToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }
}