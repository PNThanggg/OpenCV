package com.pnt.open_cv

import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import org.opencv.core.Core

class OpenCvMethodCallHandler : MethodCallHandler {
    companion object {
        private const val TAG = "OpenCvMethodCallHandler"
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("OpenCV " + Core.VERSION)
            }

            "canny_edge_detection" -> {
                val byteData: ByteArray? = call.argument("byteData")
                val threshold1: Double = call.argument("threshold1") ?: 0.0
                val threshold2: Double = call.argument("threshold2") ?: 0.0

                Log.d(TAG, "byteData $byteData")
                Log.d(TAG, "threshold1 $threshold1")
                Log.d(TAG, "threshold2 $threshold2")

                val canny: ByteArray = OpenCVCore.cannyEdgeDetection(
                    byteData,
                    threshold1,
                    threshold2,
                )

                result.success(
                    canny
                )
            }

            "adaptive_threshold" -> {
                val byteData: ByteArray? = call.argument("byteData")
                val blockSize: Int? = call.argument("blockSize")
                val c: Double? = call.argument("CValue")

                Log.d(TAG, "byteData $byteData")
                Log.d(TAG, "blockSize $blockSize")
                Log.d(TAG, "C Value $c")

                val adaptiveThreshold: ByteArray = OpenCVCore.adaptiveThreshold(
                    byteData,
                    blockSize ?: 13,
                    c ?: 5.0,
                )

                result.success(
                    AppUtils.convertWhiteToTransparent(adaptiveThreshold)
//                    adaptiveThreshold
                )
            }

            "median_blur" -> {
                val byteData: ByteArray? = call.argument("byteData")
                val kernelSize: Int? = call.argument("kernelSize")

                Log.d(TAG, "byteData $byteData")
                Log.d(TAG, "kernelSize $kernelSize")

                val medianBlur: ByteArray = OpenCVCore.medianBlur(
                    byteData,
                    kernelSize ?: 15,
                )

                result.success(
                    medianBlur
                )
            }

            "threshold" -> {
                val byteData: ByteArray? = call.argument("byteData")
                val thresh: Double? = call.argument("thresh")

                Log.d(TAG, "thresh $thresh")

                val threshold: ByteArray = OpenCVCore.threshold(
                    byteData,
                    thresh ?: 0.0
                )

                result.success(
                    threshold
                )
            }

            "remove_background" -> {
                val byteData: ByteArray? = call.argument("byteData")

                val removeBackground: ByteArray = OpenCVCore.removeBackground(
                    byteData ?: ByteArray(0),
                )

                result.success(
                    removeBackground
                )
            }

            else -> result.notImplemented()
        }
    }

//    private fun getBytesFromBitmapChunk(bitmap: Bitmap): ByteArray {
//        val bitmapSize = bitmap.rowBytes * bitmap.height
//        val byteArray = ByteArray(bitmapSize)
//        val byteBuffer = ByteBuffer.allocate(bitmapSize)
//        bitmap.copyPixelsToBuffer(byteBuffer)
//        byteBuffer.rewind()
//        byteBuffer.get(byteArray)
//        return byteArray
//    }
//
//    private fun test(src: Bitmap): Bitmap {
//        val width = src.width
//        val height = src.height
//        // create output bitmap
//        val bmOut = Bitmap.createBitmap(width, height, src.config)
//        // color information
//        var A: Int
//        var R: Int
//        var G: Int
//        var B: Int
//        var pixel: Int
//        for (x in 0 until width) {
//            for (y in 0 until height) {
//                // get pixel color
//                pixel = src.getPixel(x, y)
//                A = Color.alpha(pixel)
//                R = Color.red(pixel)
//                G = Color.green(pixel)
//                B = Color.blue(pixel)
//                var gray = (0.2989 * R + 0.5870 * G + 0.1140 * B).toInt()
//                // use 128 as threshold, above -> white, below -> black
//                gray = if (gray > 128) {
//                    255
//                } else {
//                    0
//                }
//                // set new pixel color to output bitmap
//                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray))
//            }
//        }
//        return bmOut
//    }
}