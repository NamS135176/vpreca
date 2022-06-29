package com.lifecard.vpreca.ui.ocr

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.api.GoogleVisionService
import com.lifecard.vpreca.data.vision.*
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.utils.RegexUtils
import com.lifecard.vpreca.utils.encodeImage
import com.lifecard.vpreca.utils.getScaledDownBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.UnknownHostException
import javax.inject.Inject

@Suppress("kotlin:S1192") // String literals should not be duplicated
@HiltViewModel
class CameraViewModel @Inject constructor(private val googleVisionService: GoogleVisionService) :
    ViewModel() {

    var codeOcr = MutableLiveData<String?>()
    var loading = MutableLiveData<Boolean?>()
    var error = MutableLiveData<String?>()
    var lockButtonTakePhoto = MutableLiveData<Boolean>(false)
    var networkTrouble = MutableLiveData<Boolean>()

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
                ocr = callApiGetOcr(cropped)
            }
            if (ocr == null || ocr is Result.Error) {
                val imageStream =
                    context.contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                imageStream?.close()
                ocr = callApiGetOcr(selectedImage)
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
            saveMediaToStorage(context, cropBitmap)

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

    suspend fun cropBitmap(
        context: Context,
        imageUri: Uri,
        percentTop: Float = 0f,
        percentHeight: Float = 1f
    ): Bitmap? {
        return withContext(Dispatchers.IO) {              // Dispatchers.IO (main-safety block)
            val matrix = Matrix()
            val rotationInDegrees = getRotationInDegrees(context, imageUri)
            if (rotationInDegrees != 0) {
                matrix.preRotate(rotationInDegrees.toFloat())
            }

            val bitmapStream = context.contentResolver.openInputStream(imageUri)
            val originBitmap = BitmapFactory.decodeStream(bitmapStream)
            bitmapStream?.close()
            val bitmapW = originBitmap.height
            val bitmapH = originBitmap.width
            val y = bitmapH * percentTop
            val height = bitmapH * percentHeight
            val cropBitmap = Bitmap.createBitmap(
                originBitmap,
                y.toInt(),
                0,
                height.toInt(),
                bitmapW,
                matrix,
                true
            )
            saveMediaToStorage(context, cropBitmap)

            cropBitmap
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

    private fun saveMediaToStorage(context: Context, bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }

            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        fos?.close()


        //update exif
//        imageUri?.let {
//            val imageStream =
//                requireActivity().contentResolver.openInputStream(it)
//            val exif = ExifInterface(imageStream!!)
//            exif.setAttribute(ExifInterface.TAG_ORIENTATION, "0")
//            exif.saveAttributes()
//        }
    }
}