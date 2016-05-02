package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.image.*;
import com.refactula.photomosaic.index.AverageColorIndex;
import com.refactula.photomosaic.utils.progress.PeriodicEvent;
import com.refactula.photomosaic.utils.progress.ProgressEstimator;
import com.refactula.photomosaic.utils.progress.TimeMeter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.refactula.photomosaic.app.Settings.*;
import static com.refactula.photomosaic.image.ColorChannel.*;

public class PhotoMosaicApp {

    private static ImageDataset tilesDataset;
    private static ImageDataset tinyDataset;
    private static AverageColorIndex index;

    public static void main(String[] args) throws Exception {
        System.out.println("Loading dataset...");
        TimeMeter timeMeter = TimeMeter.start();
        tinyDataset = loadTinyDataset();
        timeMeter.stop();
        System.out.println("Dataset loaded in " + TimeUnit.MILLISECONDS.toSeconds(timeMeter.get()) + "s");

        System.out.println("Loading index...");
        index = new AverageColorIndex(30);
        index.readFromFile("index.bin", tinyDataset.size());
        System.out.println("Index loaded");

        System.out.println("Creating tiles dataset...");
        tilesDataset = FileDataset.forFile("dataset.bin", TILES_COUNT, TILE_WIDTH, TILE_HEIGHT);
        System.out.println("Dataset created");

        AwtImage userImage = AwtImage.readFromResource("image.jpg");

        generate(userImage, 20, false).convert(AwtImage::new).save("png", new File("output.png"));

        System.out.println("Finished");
    }

    private static ArrayImage generate(AwtImage userImage, int maxCandidates, boolean precise) throws IOException {
        TimeMeter timeMeter;
        int cropWidth = userImage.getWidth() % TILE_WIDTH;
        int cropHeight = userImage.getHeight() % TILE_HEIGHT;
        ArrayImage tinyUserImage = ArrayImage.createHalfSizeScale(userImage.crop(
                cropWidth / 2,
                cropHeight / 2,
                userImage.getWidth() - cropWidth,
                userImage.getHeight() - cropHeight
        ));

        int horizontalTiles = tinyUserImage.getWidth() / TINY_TILE_WIDTH;
        int verticalTiles = tinyUserImage.getHeight() / TINY_TILE_HEIGHT;
        AverageColor averageColor = new AverageColor();
        Random random = new Random();

        timeMeter = TimeMeter.start();
        System.out.println("Searching for tiles...");
        PeriodicEvent periodicLog = new PeriodicEvent(3, TimeUnit.SECONDS);
        periodicLog.update();
        ProgressEstimator progressEstimator = new ProgressEstimator(TimeMeter.start());
        int readyTiles = 0;
        int[][] tiles = new int[horizontalTiles][verticalTiles];
        for (int tx = 0; tx < horizontalTiles; tx++) {
            for (int ty = 0; ty < verticalTiles; ty++) {
                Image tinyTile = tinyUserImage.crop(tx * TINY_TILE_WIDTH, ty * TINY_TILE_HEIGHT, TINY_TILE_WIDTH, TINY_TILE_HEIGHT);
                averageColor.compute(tinyTile);

                List<Integer> candidates = index.search(averageColor.get(RED), averageColor.get(GREEN), averageColor.get(BLUE), maxCandidates);


                int bestTileIndex = -1;
                if (candidates.isEmpty()) {
                    System.out.println("No candidates for color " + Integer.toHexString(averageColor.getRGB()));
                    bestTileIndex = random.nextInt(tinyDataset.size());
                } else if (precise) {
                    int bestDistance = Integer.MAX_VALUE;
                    for (Integer candidateIndex : candidates) {
                        Image candidate = tinyDataset.get(candidateIndex);
                        int distance = tinyTile.distance(candidate);
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            bestTileIndex = candidateIndex;
                        }
                    }
                } else {
                    bestTileIndex = candidates.get(random.nextInt(candidates.size()));
                }

                tiles[tx][ty] = bestTileIndex;
                readyTiles++;
                if (periodicLog.update()) {
                    double progress = readyTiles / (double) horizontalTiles / verticalTiles ;
                    long estimation = progressEstimator.estimateRemainingTime(progress);
                    System.out.format("Progress = %.3f, estimation = %ds\n", progress, TimeUnit.MILLISECONDS.toSeconds(estimation));
                }
            }
        }

        System.out.println("Generating resulting image...");
        progressEstimator = new ProgressEstimator(TimeMeter.start());
        periodicLog.update();
        readyTiles = 0;
        ArrayImage outputImage = new ArrayImage(userImage.getWidth(), userImage.getHeight());
        for (int tx = 0; tx < horizontalTiles; tx++) {
            for (int ty = 0; ty < verticalTiles; ty++) {
                Image canvas = outputImage.crop(tx * TILE_WIDTH, ty * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                canvas.copyPixels(tilesDataset.get(tiles[tx][ty]));
                readyTiles++;

                if (periodicLog.update()) {
                    double progress = readyTiles / (double) horizontalTiles / verticalTiles ;
                    long estimation = progressEstimator.estimateRemainingTime(progress);
                    System.out.format("Progress = %.3f, ready = %d, estimation = %ds\n", progress, readyTiles, TimeUnit.MILLISECONDS.toSeconds(estimation));
                }
            }
        }

        timeMeter.stop();
        System.out.println("Finished in " + TimeUnit.MILLISECONDS.toSeconds(timeMeter.get()) + "s");
        return outputImage;
    }

    private static ImageDataset loadTinyDataset() throws IOException {
        ImageDataset dataset;
        try (ImageDataset fileDataset = FileDataset.forFile("dataset-small.bin", TILES_COUNT, TINY_TILE_WIDTH, TINY_TILE_HEIGHT)) {
            dataset = InMemoryDataset.copyOf(fileDataset);
        }
        return dataset;
    }

}
