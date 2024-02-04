import 'dart:async';

import 'package:flutter/services.dart';

class OpenCV {
  static final _instance = OpenCV._internal();

  OpenCV._internal();

  static OpenCV getInstance() {
    return _instance;
  }

  static const MethodChannel _channel = MethodChannel('opencv');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Uint8List?> cannyEdgeDetection({
    required Uint8List byteData,
    required double threshold1,
    required double threshold2,
  }) async {
    final Uint8List? result = await _channel.invokeMethod(
        'canny_edge_detection', {
      'byteData': byteData,
      'threshold1': threshold1,
      'threshold2': threshold2
    });

    return result;
  }

  static Future<Uint8List?> adaptiveThreshold({
    required Uint8List byteData,
    required int blockSize,
    required double cValue,
  }) async {
    assert(blockSize > 1, "Block size > 1");
    assert(blockSize % 2 == 1, "Block size là số lẻ");

    final Uint8List? result =
        await _channel.invokeMethod('adaptive_threshold', {
      'byteData': byteData,
      'blockSize': blockSize,
      'CValue': cValue,
    });

    return result;
  }

  static Future<Uint8List?> medianBlur({
    required Uint8List byteData,
    required int kernelSize,
  }) async {
    final Uint8List? result =
        await _channel.invokeMethod('median_blur', {
      'byteData': byteData,
      'kernelSize': kernelSize,
    });

    return result;
  }
}
