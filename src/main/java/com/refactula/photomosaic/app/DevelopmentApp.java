package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AwtImage;

import java.io.DataInputStream;
import java.io.EOFException;

public class DevelopmentApp {

    public static void main(String[] args) throws Exception {
        ArrayImage buffer = new ArrayImage(EightyMillionTinyImages.IMAGE_WIDTH, EightyMillionTinyImages.IMAGE_HEIGHT);
        AwtImage awtImage = new AwtImage(EightyMillionTinyImages.IMAGE_WIDTH, EightyMillionTinyImages.IMAGE_HEIGHT);

        ImageDataset imageDataset = new EightyMillionTinyImages();

        try (DataInputStream input = imageDataset.openDataInputStream(0)) {
            while (true) {
                buffer.readFrom(input);
                awtImage.copyPixels(buffer);
                awtImage.scale(8).display();
            }
        } catch (EOFException ignored) {}
    }

}
