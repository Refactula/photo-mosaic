package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.AwtImage;
import com.refactula.photomosaic.image.Image;

import java.util.Iterator;

public class DevelopmentApp {

    public static void main(String[] args) throws Exception {
        ImageDataset dataset = new EightyMillionTinyImages();
        AwtImage awtImage = new AwtImage(EightyMillionTinyImages.IMAGE_WIDTH, EightyMillionTinyImages.IMAGE_HEIGHT);
        for (Iterator<Image> iterator = dataset.iterator(0); iterator.hasNext(); ) {
            Image image = iterator.next();
            awtImage.copyPixels(image);
            awtImage.scale(8).display();
        }
    }

}
