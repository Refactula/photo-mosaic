package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.Image;

import java.io.IOException;

/**
 * Abstract ImageDataset for such datasets that by nature access images sequentially
 * and require extra time for random access.
 */
public abstract class StreamImageDataset implements ImageDataset {

    private int currentIndex = -1;

    @Override
    public Image get(int index) throws IOException {
        if (currentIndex != index) {
            changePosition(index);
            currentIndex = index;
        }

        currentIndex++;
        return next();
    }

    /**
     * Move the current dataset pointer into given position.
     */
    protected abstract void changePosition(int index) throws IOException;

    /**
     * Reads the next image from dataset.
     */
    protected abstract Image next() throws IOException;

}
