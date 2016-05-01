package com.refactula.photomosaic.dataset;

public abstract class AbstractImageDataset implements ImageDataset {

    private final int size;
    private final int imageWidth;
    private final int imageHeight;

    public AbstractImageDataset(int size, int imageWidth, int imageHeight) {
        this.size = size;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public int getImageHeight() {
        return imageHeight;
    }

}
