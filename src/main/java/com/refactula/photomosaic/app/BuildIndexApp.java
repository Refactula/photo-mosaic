package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AverageColor;
import com.refactula.photomosaic.image.ColorChannel;
import com.refactula.photomosaic.utils.progress.PeriodicEvent;
import com.refactula.photomosaic.utils.progress.ProgressEstimator;
import com.refactula.photomosaic.utils.progress.TimeMeter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import static com.refactula.photomosaic.app.Settings.*;

public class BuildIndexApp {

    public static void main(String[] args) throws Exception {
//        ImageDataset dataset;
//        try (ImageDataset fileDataset = FileDataset.forFile("dataset.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT)) {
//            dataset = InMemoryDataset.copyOf(fileDataset);
//        }

        PeriodicEvent periodicLog = new PeriodicEvent(3, TimeUnit.SECONDS);
        periodicLog.update();
        ProgressEstimator progressEstimator = new ProgressEstimator(TimeMeter.start());

        try (
                DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("index.bin")));
                ImageDataset dataset = FileDataset.forFile("dataset.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT)
        ) {
            ArrayImage buffer = dataset.createImageBuffer();
            AverageColor averageColor = new AverageColor();

            for (int i = 0; i < dataset.size(); i++) {
                if (!dataset.load(i, buffer)) {
                    throw new RuntimeException("Dataset size is invalid");
                }
                averageColor.compute(buffer);
                for (ColorChannel channel : ColorChannel.values()) {
                    output.writeByte(averageColor.get(channel));
                }

                if (periodicLog.update()) {
                    double progress = i / (double) dataset.size();
                    long estimationSeconds = TimeUnit.MILLISECONDS.toSeconds(progressEstimator.estimateRemainingTime(progress));
                    System.out.format("Progress = %.3f, remaining time = %ds\n", progress, estimationSeconds);
                }
            }
        }
    }

}
