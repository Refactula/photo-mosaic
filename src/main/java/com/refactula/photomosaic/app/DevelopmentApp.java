package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.image.*;
import com.refactula.photomosaic.index.AverageColorIndex;

import java.io.IOException;

public class DevelopmentApp {

    public static final int IMAGES_COUNT = 100000;
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;

    public static void main(String[] args) throws Exception {
        ImageDataset dataset = loadDataset();
        AverageColorIndex index = AverageColorIndex.readFromFile("index.bin", dataset.size());
        AwtImage userImage = AwtImage.readFromResource("image.jpg");

        int cropWidth = userImage.getWidth() % TILE_WIDTH;
        int cropHeight = userImage.getHeight() % TILE_HEIGHT;
        ArrayImage result = userImage.crop(
                cropWidth / 2,
                cropHeight / 2,
                userImage.getWidth() - cropWidth,
                userImage.getHeight() - cropHeight
        ).convert(ArrayImage::new);

        int horizontalTiles = result.getWidth() / TILE_WIDTH;
        int verticalTiles = result.getHeight() / TILE_HEIGHT;
        AverageColor averageColor = new AverageColor();
        for (int tx = 0; tx < horizontalTiles; tx++) {
            for (int ty = 0; ty < verticalTiles; ty++) {
                Image tileCanvas = result.crop(tx * TILE_WIDTH, ty * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                averageColor.compute(tileCanvas);

                int minDistance = Integer.MAX_VALUE;
                int minTile = -1;
                for (int i = 0; i < index.size(); i++) {
                    int distance = 0;
                    for (ColorChannel channel : ColorChannel.values()) {
                        distance += Math.abs(averageColor.get(channel) - index.get(i, channel));
                    }
                    if (distance < minDistance) {
                        minDistance = distance;
                        minTile = i;
                    }
                }

                tileCanvas.copyPixels(dataset.get(minTile));
            }
        }

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
