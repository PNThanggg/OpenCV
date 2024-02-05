package com.pnt.open_cv

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import java.io.ByteArrayOutputStream
import kotlin.math.abs


object AppUtils {
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun convertWhiteToTransparent(byteArray: ByteArray): ByteArray {
        val bitmap: Bitmap = byteArrayToBitmap(byteArray = byteArray)
        val newBitmap: Bitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixelColor: Int = bitmap.getPixel(x, y)

                if (isColorInWhiteRange(pixelColor, 0)) {
                    newBitmap.setPixel(x, y, Color.TRANSPARENT)
                } else {
                    newBitmap.setPixel(x, y, pixelColor)
                }
            }
        }

        return bitmapToByteArray(bitmap = newBitmap)
    }

    private fun isColorInWhiteRange(color: Int, threshold: Int): Boolean {
        // Tách các thành phần màu ra từ giá trị màu đầu vào
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        // Tính độ sáng trung bình của màu
        val average = (red + green + blue) / 3

        // Kiểm tra xem các thành phần màu có nằm gần giá trị độ sáng trung bình (với một ngưỡng nào đó) không
        // Đồng thời kiểm tra xem độ sáng trung bình có cao hơn một ngưỡng nào đó (để coi là gần trắng) không
        return abs(red - average) < threshold && abs(green - average) < threshold && abs(
            blue - average
        ) < threshold && average > 255 - threshold
    }
}