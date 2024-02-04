package com.pnt.open_cv

import io.flutter.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.opencv.android.OpenCVLoader

class MainActivity : FlutterActivity() {
    companion object {
        private const val OPEN_CV_METHOD_CHANNEL_NAME = "opencv"
        private const val TAG = "MainActivity"
    }

    private var openCvMethodChannel: MethodChannel? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        if (!OpenCVLoader.initLocal()) {
            Log.e(TAG, "OpenCV Loader success")
        }

        openCvMethodChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            OPEN_CV_METHOD_CHANNEL_NAME
        )
        openCvMethodChannel?.setMethodCallHandler(
            OpenCvMethodCallHandler()
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        openCvMethodChannel?.setMethodCallHandler(null)
        openCvMethodChannel = null
    }
}
