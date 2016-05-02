package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.utils.progress.PeriodicEvent;
import com.refactula.photomosaic.utils.progress.ProgressEstimator;
import com.refactula.photomosaic.utils.progress.TimeMeter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import static com.refactula.photomosaic.app.Settings.*;

public class DownloadImagesApp {

    public static void main(String[] args) throws Exception {
        TimeMeter timeMeter = TimeMeter.start();
        ProgressEstimator progressEstimator = new ProgressEstimator(timeMeter);
        PeriodicEvent periodicLog = new PeriodicEvent(3, TimeUnit.SECONDS);
        periodicLog.update();

        try (
                ImageDataset dataset = new EightyMillionTinyImages();
                DataOutputStream tilesDataset = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dataset.bin")));
                DataOutputStream tinyDataset = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dataset-small.bin")))
        ) {
            ArrayImage sourceBuffer = new ArrayImage(dataset.getImageWidth(), dataset.getImageHeight());
            ArrayImage tile = new ArrayImage(TILE_WIDTH, TILE_HEIGHT);
            ArrayImage tinyTile = new ArrayImage(TINY_TILE_WIDTH, TINY_TILE_HEIGHT);

            for (int i = 0; i < TILES_COUNT; i++) {
                dataset.load(i, sourceBuffer);
                sourceBuffer.scaleHalfSize(tile);
                tile.scaleHalfSize(tinyTile);

                tile.writeTo(tilesDataset);
                tinyTile.writeTo(tinyDataset);

                if (periodicLog.update()) {
                    long remainingEstimation = progressEstimator.estimateRemainingTime(i / (double) TILES_COUNT);
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
