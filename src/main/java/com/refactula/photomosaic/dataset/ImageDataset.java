package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.Image;

import java.io.Closeable;
import java.io.IOException;

public interface ImageDataset extends Closeable {

    Image get(int index) throws IOException;

}
