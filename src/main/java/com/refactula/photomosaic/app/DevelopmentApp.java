package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.image.*;
import com.refactula.photomosaic.index.AverageColorIndex;
import com.refactula.photomosaic.utils.progress.TimeMeter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.refactula.photomosaic.image.ColorChannel.*;

public class DevelopmentApp {

    public static final int IMAGES_COUNT = 100000;
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;

    public static void main(String[] args) throws Exception {
        ImageDataset dataset = loadDataset();

        AverageColorIndex index = new AverageColorIndex(20, 1000);
        index.readFromFile("index.bin", dataset.size());

        AwtImage userImage = AwtImage.readFromResource("image.jpg");

        int cropWidth = userImage.getWidth() % TILE_WIDTH;
        int cropHeight = userImage.getHeight() % TILE_HEIGHT;
        ArrayImage result = userImage.crop(
                cropWidth / 2,
                cropHeight / 2,
                userImage.getWidth() - cropWidth,
                userImage.getHeight() - cropHeight
        ).convert(ArrayImage::new);

        TimeMeter timeMeter = TimeMeter.start();

        int horizontalTiles = result.getWidth() / TILE_WIDTH;
        int verticalTiles = result.getHeight() / TILE_HEIGHT;
        AverageColor averageColor = new AverageColor();

        for (int tx = 0; tx < horizontalTiles; tx++) {
            for (int ty = 0; ty < verticalTiles; ty++) {
                Image tileCanvas = result.crop(tx * TILE_WIDTH, ty * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                averageColor.compute(tileCanvas);

                List<Integer> candidates = index.search(averageColor.get(RED), averageColor.get(GREEN), averageColor.get(BLUE));
                if (candidates.isEmpty()) {
                    throw new RuntimeException("No candidates");
                }

                Image bestTile = null;
                int bestDistance = Integer.MAX_VALUE;
                for (Integer candidateIndex : candidates) {
                    Image candidate = dataset.get(candidateIndex);
                    int distance = tileCanvas.distance(candidate);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestTile = candidate;
                    }
                }

                tileCanvas.copyPixels(bestTile);
            }
        }

        timeMeter.stop();
        System.out.println("Finished in " + TimeUnit.MILLISECONDS.toSeconds(timeMeter.get()) + "s");

        result.convert(AwtImage::new).display();
    }

    private static ImageDataset loadDataset() throws IOException {
        ImageDataset dataset;
        try (ImageDataset fileDataset = FileDataset.forFile("dataset.bin", IMAGES_COUNT, TILE_WIDTH, TILE_HEIGHT)) {
            dataset = InMemoryDataset.copyOf(fileDataset);
        }
        return dataset;
    }

}
