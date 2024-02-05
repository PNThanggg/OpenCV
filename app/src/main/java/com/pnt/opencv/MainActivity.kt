package com.pnt.opencv

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Core.LUT
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.Mat
import org.opencv.core.Size
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun reduceImageColors(view: View) {
        val options = BitmapFactory.Options()
        options.inScaled = false // Leaving it to true enlarges the decoded image size.

        val original = BitmapFactory.decodeResource(resources, R.drawable.part3, options)

        val img1 = Mat()
        Utils.bitmapToMat(original, img1)

        val result: Mat = reduceColors(img1, 80, 15, 10)

        val imgBitmap = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(result, imgBitmap)

        val imageView = findViewById<ImageView>(R.id.opencvImg)
        imageView.setImageBitmap(imgBitmap)
        saveBitmap(imgBitmap, "reduce_colors")
    }

    private fun createLUT(numColors: Int): Mat? {
        // When numColors=1 the LUT will only have 1 color which is black.
        if (numColors < 0 || numColors > 256) {
            println("Invalid Number of Colors. It must be between 0 and 256 inclusive.")
            return null
        }

        val lookupTable: Mat = Mat.zeros(Size(1.0, 256.0), CV_8UC1)
        var startIdx = 0
        var x = 0
        while (x < 256) {
            lookupTable.put(x, 0, x.toDouble())
            for (y in startIdx until x) {
                if (lookupTable[y, 0][0] == 0.0) {
                    lookupTable.put(y, 0, *lookupTable[x, 0])
                }
            }
            startIdx = x
            x = (x + 256.0 / numColors).toInt()
        }
        return lookupTable
    }

    private fun reduceColors(img: Mat, numRed: Int, numGreen: Int, numBlue: Int): Mat {
        val redLUT: Mat? = createLUT(numRed)
        val greenLUT: Mat? = createLUT(numGreen)
        val blueLUT: Mat? = createLUT(numBlue)
        val BGR: List<Mat> = ArrayList(3)
        Core.split(img, BGR) // splits the image into its channels in the List of Mat arrays.
        LUT(BGR[0], blueLUT, BGR[0])
        LUT(BGR[1], greenLUT, BGR[1])
        LUT(BGR[2], redLUT, BGR[2])
        Core.merge(BGR, img)
        return img
    }


    private fun saveBitmap(imgBitmap: Bitmap, fileNameOpening: String) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
        val now = Date()
        val fileName = fileNameOpening + "_" + formatter.format(now) + ".jpg"
        val outStream: FileOutputStream
        try {
            // Get a public path on the device storage for saving the file. Note that the word external does not mean the file is saved in the SD card. It is still saved in the internal storage.
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            // Creates directory for saving the image.
            val saveDir = File("$path/HeartBeat/")

            // If the directory is not created, create it.
            if (!saveDir.exists()) saveDir.mkdirs()

            // Create the image file within the directory.
            val fileDir = File(saveDir, fileName) // Creates the file.

            // Write into the image file by the BitMap content.
            outStream = FileOutputStream(fileDir)
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            MediaScannerConnection.scanFile(
                this.applicationContext, arrayOf(fileDir.toString()), null
            ) { path, uri -> }
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}