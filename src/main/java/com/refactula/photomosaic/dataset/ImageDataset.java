package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.Image;

import java.io.Closeable;
import java.io.IOException;

public interface ImageDataset extends Closeable {

    default boolean load(int index, ArrayImage destination) throws IOException {
        Image buffer = get(index);
        if (buffer == null) {
            return false;
        }
        destination.copyPixels(buffer);
        return true;
    }

    default Image get(int index) throws IOException {
        ArrayImage buffer = createImageBuffer();
        return load(index, buffer) ? buffer : null;
    }

    int getImageWidth();

    int getImageHeight();

    default ArrayImage createImageBuffer() {
        return new ArrayImage(getImageWidth(), getImageHeight());
    }

}
