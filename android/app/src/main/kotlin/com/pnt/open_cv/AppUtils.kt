package com.pnt.open_cv

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


object AppUtils {
    fun byteDataToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }
}