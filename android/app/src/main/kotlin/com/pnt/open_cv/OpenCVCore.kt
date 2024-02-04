package com.pnt.open_cv

import android.graphics.Bitmap
import io.flutter.Log
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C
import org.opencv.imgproc.Imgproc.THRESH_BINARY


object OpenCVCore {
    private const val TAG = "CVCore"
    private const val JPG = ".jpg"

    fun cannyEdgeDetection(
        byteData: ByteArray?, threshold1: Double, threshold2: Double
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
        byteData: ByteArray?, blockSize: Int, cValue: Double
    ): ByteArray {
        var byteArray = ByteArray(0)

        try {
            val dst = Mat()
            val src = Imgcodecs.imdecode(MatOfByte(byteData), Imgcodecs.IMREAD_GRAYSCALE)

            Imgproc.adaptiveThreshold(
                src, dst, 255.0, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, blockSize, cValue
            )

            val matOfByte = MatOfByte()
            Imgcodecs.imencode(JPG, dst, matOfByte)
            byteArray = matOfByte.toArray()
        } catch (e: Exception) {
            Log.e(TAG, "OpenCV Error: $e")
            println("OpenCV Error: $e")
        }

        return byteArray
    }

    fun threshold(
        byteData: ByteArray?,
        thresh: Double,
    ): ByteArray {
        var byteArray = ByteArray(0)

        try {
            val dst = Mat()
            val src = Imgcodecs.imdecode(MatOfByte(byteData), Imgcodecs.IMREAD_GRAYSCALE)

//            Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY)

            Imgproc.threshold(
                src, dst, thresh, 255.0, THRESH_BINARY
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

    fun removeBackground(byteData: ByteArray): ByteArray {
        var bitmap: Bitmap = AppUtils.byteDataToBitmap(byteData)

        try {
            val dst = Mat()
            Utils.bitmapToMat(bitmap, dst)

            val row = dst.rows()
            val column = dst.cols()

            val p1 = Point(1.0 * column / 100, 1.0 * row / 100)
            val p2 = Point(1.0 * column - 1.0 * column / 100, 1.0 * row - 1.0 * row / 100)
            val rect = Rect(p1, p2)

            val mask = Mat()
            val fgdModel = Mat()
            val bgdModel = Mat()
            val imgC3 = Mat()

            Imgproc.cvtColor(dst, imgC3, Imgproc.COLOR_RGBA2RGB)
            Imgproc.grabCut(imgC3, mask, rect, bgdModel, fgdModel, 5, Imgproc.GC_INIT_WITH_RECT)
            val source = Mat(1, 1, CvType.CV_8U, Scalar(3.0))
            Core.compare(mask, source, mask, Core.CMP_EQ)

            val foreground = Mat(
                dst.size(), CvType.CV_8UC3, Scalar(
                    255.0, 255.0, 255.0, 255.0
                )
            )

            dst.copyTo(foreground, mask)

            bitmap = Bitmap.createBitmap(
                foreground.size().width.toInt(),
                foreground.size().height.toInt(),
                Bitmap.Config.ARGB_8888
            )

            Utils.matToBitmap(foreground, bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "OpenCV Error: $e")
            println("OpenCV Error: $e")
        }

        return AppUtils.bitmapToByteArray(bitmap)
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