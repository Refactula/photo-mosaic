package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.Image;

import java.util.Iterator;
import java.util.stream.Stream;

public interface ImageDataset {

    /** Total amount of images */
    int size();

    Iterator<Image> iterator(int fromIndex);

}
