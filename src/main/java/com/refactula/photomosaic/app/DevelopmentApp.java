package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AwtImage;
import com.refactula.photomosaic.image.Image;

import java.io.DataInputStream;
import java.io.EOFException;
import java.util.Random;

public class DevelopmentApp {

    public static void main(String[] args) throws Exception {
        AwtImage awtImage = new AwtImage(EightyMillionTinyImages.IMAGE_WIDTH, EightyMillionTinyImages.IMAGE_HEIGHT);
        try (ImageDataset dataset = new EightyMillionTinyImages()) {
            Random random = new Random();
            while (true) {
                Image image = dataset.get(random.nextInt(EightyMillionTinyImages.SIZE));
                awtImage.copyPixels(image);
                awtImage.scale(8).display("8 times bigger");
                awtImage.scale(0.5).display("16x16");
            }
        }
    }

}
