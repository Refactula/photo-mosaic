package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;

import java.io.Closeable;
import java.io.IOException;

public interface ImageDataset extends Closeable {

    boolean load(int index, ArrayImage destination) throws IOException;

    int getImageWidth();

    int getImageHeight();

}
