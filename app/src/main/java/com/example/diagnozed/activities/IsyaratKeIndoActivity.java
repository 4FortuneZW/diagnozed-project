package com.example.diagnozed.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.example.diagnozed.databinding.ActivityIsyaratKeIndoBinding;
import com.example.diagnozed.utilities.Draw;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions;

import java.util.concurrent.ExecutionException;

public class IsyaratKeIndoActivity extends AppCompatActivity {

    private ActivityIsyaratKeIndoBinding binding;

    private ObjectDetector objectDetector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private LocalModel localModel;

//    private LocalModel localModel = new LocalModel.Builder()
//            .setAssetFilePath("firstModel.tflite")
//            .build();
//
//    CustomObjectDetectorOptions customObjectDetectorOptions =
//            new CustomObjectDetectorOptions.Builder(localModel)
//                    .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
//                    .enableClassification()
//                    .setClassificationConfidenceThreshold(0.5f)
//                    .setMaxPerObjectLabelCount(3)
//                    .build();
//
//    ObjectDetector objectDetector =
//            ObjectDetection.getClient(customObjectDetectorOptions);


    @Override
    @ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIsyaratKeIndoBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());

        cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindPreview(cameraProvider);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                ContextCompat.getMainExecutor(getApplicationContext())
        );

        localModel = new LocalModel.Builder()
                .setAssetFilePath("tfHubModel.tflite")
                .build();

        CustomObjectDetectorOptions customObjectDetectorOptions =
            new CustomObjectDetectorOptions.Builder(localModel)
                    .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                    .enableClassification()
                    .setClassificationConfidenceThreshold(0.5f)
                    .setMaxPerObjectLabelCount(3)
                    .build();

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions);

    }

    @ExperimentalGetImage
    private void bindPreview(ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280,720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getApplicationContext()),
                image -> {
                    int rotationDegrees = image.getImageInfo().getRotationDegrees();

                    Image img = image.getImage();

                    if (img != null) {
                        InputImage processImage = InputImage.fromMediaImage(img, rotationDegrees);

                        objectDetector
                                .process(processImage)
                                .addOnSuccessListener(results -> {

                                    if (binding.iskinLayout.getChildCount() > 1) {
                                        binding.iskinLayout.removeViewAt(1);
                                    }

                                    for(DetectedObject detectedObject : results) {
                                        Rect rect = detectedObject.getBoundingBox();
                                        Draw element = new Draw(
                                                getApplicationContext(),
                                                rect,
                                                detectedObject.getLabels().toString());
                                        binding.iskinLayout.addView(element,1);
                                    }

                                    image.close();
                                }).addOnFailureListener(e -> image.close());
                    }
                }
        );

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
//        Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();
    }
}