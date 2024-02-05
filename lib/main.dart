import 'dart:async';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_cache_manager/flutter_cache_manager.dart';

import 'open_cv/opencv.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  Image image = Image.asset('assets/temp.png');
  Image imageCanny = Image.asset('assets/temp.png');
  Image imageAdaptiveThreshold = Image.asset('assets/temp.png');
  Image imageMedianBlur = Image.asset('assets/temp.png');
  Image imageThreshold = Image.asset('assets/temp.png');
  File? file;
  bool preloaded = false;
  bool loaded = false;

  List<String> urls = [
    "https://i.pinimg.com/564x/54/e2/ae/54e2aeefa75d031813ec56f6b3efc9ad.jpg",
    "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/sudoku.png",
    "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/left.jpg",
    "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/left01.jpg",
    "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/right01.jpg",
    "https://raw.githubusercontent.com/opencv/opencv/master/samples/data/smarties.png",
  ];
  int urlIndex = 0;

  @override
  void initState() {
    super.initState();
    loadImage();
    initPlatformState();
  }

  Future loadImage() async {
    file = await DefaultCacheManager().getSingleFile(urls[urlIndex]);

    if (urlIndex >= urls.length - 1) {
      urlIndex = 0;
    } else {
      urlIndex++;
    }

    setState(() {
      image = Image.file(file!);
      preloaded = true;
    });
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await OpenCV.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> runFunction() async {
    try {
      Uint8List? canny = await OpenCV.cannyEdgeDetection(
        byteData: await file!.readAsBytes(),
        threshold1: 50,
        threshold2: 150,
      );

      Uint8List? adaptiveThreshold = await OpenCV.adaptiveThreshold(
        byteData: await file!.readAsBytes(),
        blockSize: 15,
        cValue: 5.0,
      );

      Uint8List? medianBlur = await OpenCV.medianBlur(
        byteData: await file!.readAsBytes(),
        kernelSize: 15,
      );

      Uint8List? threshold = await OpenCV.threshold(
        byteData: await file!.readAsBytes(),
        thresh: 5.0,
      );

      setState(() {
        if (canny != null) {
          imageCanny = Image.memory(canny);
        }

        if (adaptiveThreshold != null) {
          imageAdaptiveThreshold = Image.memory(adaptiveThreshold);
        }

        if (medianBlur != null) {
          imageMedianBlur = Image.memory(medianBlur);
        }

        if (threshold != null) {
          imageThreshold = Image.memory(threshold);
        }

        loaded = true;
      });
    } on PlatformException {
      log("$PlatformException");
    }

    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            loadImage();
          },
          child: const Icon(Icons.refresh),
        ),
        body: ListView(
          children: <Widget>[
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
            const Text("Before"),
            preloaded
                ? image
                : const Text("There might be an error in loading your asset."),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                TextButton(
                  onPressed: () {
                    runFunction();
                  },
                  child: const Text('Run'),
                ),
              ],
            ),
            const Text("After"),
            loaded
                ? Container(
                    color: Colors.black,
                    padding: const EdgeInsets.all(10.0),
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    child: imageCanny,
                  )
                : Container(),
            loaded
                ? Container(
                    color: Colors.green,
                    padding: const EdgeInsets.all(10.0),
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    child: imageAdaptiveThreshold,
                  )
                : Container(),
            loaded
                ? Container(
                    color: Colors.black,
                    padding: const EdgeInsets.all(10.0),
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    child: imageMedianBlur,
                  )
                : Container(),
            loaded
                ? Container(
                    color: Colors.black,
                    padding: const EdgeInsets.all(10.0),
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    child: imageThreshold,
                  )
                : Container(),
          ],
        ),
      ),
    );
  }
}
