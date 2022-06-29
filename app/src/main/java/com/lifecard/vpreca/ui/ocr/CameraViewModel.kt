package com.lifecard.vpreca.ui.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
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
            awaitAll(
                async { }
            )
        }
        viewModelScope.launch {
            loading.value = true

            var ocr: Result<String>? = null
            cropBitmap(context, imageUri, percentTop, percentHeight)?.let { cropped ->
                ocr = mlKitDetectOcr(cropped)
                if (ocr == null || ocr is Result.Error) {
                    ocr = callApiGetOcr(cropped)
                }
            }
            if (ocr == null || ocr is Result.Error) {
                val imageStream =
                    context.contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                ocr = mlKitDetectOcr(selectedImage)
                if (ocr == null || ocr is Result.Error) {
                    ocr = callApiGetOcr(selectedImage)
                }
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

    fun getCodeByGoogleVisionOcrByBitmap(
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
                ocr = callApiGetOcr(cropped)
            }
            if (ocr == null || ocr is Result.Error) {
                ocr = callApiGetOcr(bitmap)
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
                        languageHints = "ja",
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
                    Result.Success(codeResult)
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
            //1. delete all ( + ) + : + spacing on text
            textAnnotations.forEach {
                it.description = it.description.trim().replace(Regex("[\\(\\)\\s:]"), "")
                //remove all not alphabet letter and number
                it.description = it.description.replace(Regex("[^A-z0-9]"), "")
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

    private fun cropBitmap(
        context: Context,
        imageUri: Uri,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ): Bitmap? {
        try {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (path.startsWith("content://")) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    return inputStream?.let { ExifInterface(it) }
                }
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