package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.utils.PeriodicEvent;
import com.refactula.photomosaic.utils.ProgressEstimator;
import com.refactula.photomosaic.utils.TimeMeter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class DownloadImagesApp {

    public static final int DATASET_SIZE = 100000;

    public static void main(String[] args) throws Exception {
        TimeMeter timeMeter = TimeMeter.start();
        ProgressEstimator progressEstimator = new ProgressEstimator(timeMeter);
        PeriodicEvent periodicLog = new PeriodicEvent(3, TimeUnit.SECONDS);
        periodicLog.update();

        try (
                ImageDataset dataset = new EightyMillionTinyImages();
                DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dataset.bin")))
        ) {
            ArrayImage sourceBuffer = new ArrayImage(dataset.getImageWidth(), dataset.getImageHeight());
            ArrayImage resultBuffer = new ArrayImage(sourceBuffer.getWidth() / 2, sourceBuffer.getHeight() / 2);

            for (int i = 0; i < DATASET_SIZE; i++) {
                dataset.load(i, sourceBuffer);
                sourceBuffer.scaleHalfSize(resultBuffer);
                resultBuffer.writeTo(output);

                if (periodicLog.update()) {
                    long remainingEstimation = progressEstimator.estimateRemainingTime(i / (double) DATASET_SIZE);
                    System.out.println("Time spend: " + TimeUnit.MILLISECONDS.toSeconds(timeMeter.get()) + "s"
                            + ", remaining time: " + TimeUnit.MILLISECONDS.toSeconds(remainingEstimation) + "s"
                    );
                }
            }
        }

        timeMeter.stop();
        System.out.println("Time spend: " + timeMeter.get());
    }

}
