package com.lifecard.vpreca.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream


/**
 * @param bitmap the Bitmap to be scaled
 * @param threshold the maxium dimension (either width or height) of the scaled bitmap
 * @param isNecessaryToKeepOrig is it necessary to keep the original bitmap? If not recycle the original bitmap to prevent memory leak.
 */
fun Bitmap.getScaledDownBitmap(threshold: Int, isNecessaryToKeepOrig: Boolean): Bitmap? {
    val width = this.width
    val height = this.height
    var newWidth = width
    var newHeight = height
    if (width > height && width > threshold) {
        newWidth = threshold
        newHeight = (height * newWidth.toFloat() / width).toInt()
    }
    if (width > height && width <= threshold) {
        //the bitmap is already smaller than our required dimension, no need to resize it
        return this
    }
    if (width < height && height > threshold) {
        newHeight = threshold
        newWidth = (width * newHeight.toFloat() / height).toInt()
    }
    if (width < height && height <= threshold) {
        //the bitmap is already smaller than our required dimension, no need to resize it
        return this
    }
    if (width == height && width > threshold) {
        newWidth = threshold
        newHeight = newWidth
    }
    return if (width == height && width <= threshold) {
        //the bitmap is already smaller than our required dimension, no need to resize it
        this
    } else getResizedBitmap(this, newWidth, newHeight, isNecessaryToKeepOrig)
}

private fun getResizedBitmap(
    bm: Bitmap,
    newWidth: Int,
    newHeight: Int,
    isNecessaryToKeepOrig: Boolean
): Bitmap? {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    if (!isNecessaryToKeepOrig) {
        bm.recycle()
    }
    return resizedBitmap
}

fun Bitmap.encodeImage(): String? {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
}