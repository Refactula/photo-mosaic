package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AwtImage;

import java.io.File;
import java.util.Random;

import static com.refactula.photomosaic.app.Settings.*;

public class DisplayImagesApp {

    public static void main(String[] args) throws Exception {
        try (ImageDataset dataset = FileDataset.forFile(new File("dataset.bin"), TILES_COUNT, TILE_WIDTH, TILE_HEIGHT)) {
            ArrayImage buffer = new ArrayImage(dataset.getImageWidth(), dataset.getImageHeight());
            AwtImage awtImage = new AwtImage(buffer.getWidth(), buffer.getHeight());
            Random random = new Random();
            int index;
            while (dataset.load(index = random.nextInt(dataset.size()), buffer)) {
                awtImage.copyPixels(buffer);
                awtImage.scale(8).display("8 times bigger - " + index);
            }
        }
    }

}
