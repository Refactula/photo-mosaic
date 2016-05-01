package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AwtImage;

import java.io.File;
import java.util.Random;

public class DisplayImagesApp {

    public static void main(String[] args) throws Exception {
        try (ImageDataset dataset = FileDataset.forFile(new File("dataset.bin"), 100000, 16, 16)) {
            ArrayImage buffer = new ArrayImage(dataset.getImageWidth(), dataset.getImageHeight());
            AwtImage awtImage = new AwtImage(buffer.getWidth(), buffer.getHeight());
            Random random = new Random();
            while (dataset.load(random.nextInt(dataset.size()), buffer)) {
                awtImage.copyPixels(buffer);
                awtImage.scale(8).display("8 times bigger");
            }
        }
    }

}
