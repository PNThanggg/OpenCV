package com.pnt.open_cv

import io.flutter.Log
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C
import org.opencv.imgproc.Imgproc.THRESH_BINARY

object OpenCVCore {
    private const val TAG = "CVCore"
    private const val JPG = ".jpg"

    fun cannyEdgeDetection(
        byteData: ByteArray?,
        threshold1: Double,
        threshold2: Double
    ): ByteArray {
        var byteArray = ByteArray(0)

        try {
            val dst = Mat()
            val src = Imgcodecs.imdecode(MatOfByte(byteData), Imgcodecs.IMREAD_UNCHANGED)

            Imgproc.Canny(src, dst, threshold1, threshold2)

            val matOfByte = MatOfByte()
            Imgcodecs.imencode(JPG, dst, matOfByte)
            byteArray = matOfByte.toArray()
        } catch (e: Exception) {
            Log.e(TAG, "OpenCV Error: $e")
            println("OpenCV Error: $e")
        }

        return byteArray
    }

    fun adaptiveThreshold(
        byteData: ByteArray?,
        blockSize: Int,
        cValue: Double
    ): ByteArray {
        var byteArray = ByteArray(0)

        try {
            val dst = Mat()
            val src = Imgcodecs.imdecode(MatOfByte(byteData), Imgcodecs.IMREAD_GRAYSCALE)

            Imgproc.adaptiveThreshold(
                src,
                dst,
                255.0,
                ADAPTIVE_THRESH_MEAN_C,
                THRESH_BINARY,
                blockSize,
                cValue
            )

//            Imgproc.medianBlur(src, dst, 15)
//            Imgproc.adaptiveThreshold(
//                dst,
//                dst,
//                255.0,
//                ADAPTIVE_THRESH_MEAN_C,
//                THRESH_BINARY,
//                blockSize,
//                cValue
//            )

            val matOfByte = MatOfByte()
            Imgcodecs.imencode(JPG, dst, matOfByte)
            byteArray = matOfByte.toArray()
        } catch (e: Exception) {
            Log.e(TAG, "OpenCV Error: $e")
            println("OpenCV Error: $e")
        }

        return byteArray
    }

    fun medianBlur(
        byteData: ByteArray?,
        kernelSize: Int,
    ): ByteArray {
        var byteArray = ByteArray(0)

        try {
            val dst = Mat()
            val src = Imgcodecs.imdecode(MatOfByte(byteData), Imgcodecs.IMREAD_GRAYSCALE)

            Imgproc.medianBlur(src, dst, kernelSize)

            val matOfByte = MatOfByte()
            Imgcodecs.imencode(JPG, dst, matOfByte)
            byteArray = matOfByte.toArray()
        } catch (e: Exception) {
            Log.e(TAG, "OpenCV Error: $e")
            println("OpenCV Error: $e")
        }

        return byteArray
    }
}