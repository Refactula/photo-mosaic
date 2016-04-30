package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.EightyMillionTinyImages;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.image.AwtImage;

public class DevelopmentApp {

    public static void main(String[] args) throws Exception {
        ImageDataset dataset = new EightyMillionTinyImages();
        AwtImage awtImage = new AwtImage(EightyMillionTinyImages.IMAGE_WIDTH, EightyMillionTinyImages.IMAGE_HEIGHT);

        dataset.stream(79302016).forEach(image -> {
            awtImage.copyPixels(image);
            awtImage.scale(8).display();
        });
    }

}
