package com.refactula.photomosaic.image;

import java.util.function.Supplier;

public interface Image {

    int getWidth();

    int getHeight();

    /** Returns RGB value of given pixel */
    int getRGB(int x, int y);

    void setRGB(int x, int y, int rgb);

    /** Returns the color channel value of given pixel in range [0-256) */
    int get(ColorChannel colorChannel, int x, int y);

    void set(ColorChannel colorChannel, int x, int y, int value);

    default Image crop(int x, int y, int width, int height) {
        return new CropImage(this, x, y, width, height);
    }

    default void copyPixels(Image source) {
        if (getWidth() != source.getWidth() || getHeight() != source.getHeight()) {
            throw new IllegalArgumentException("Images must have same size");
        }

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setRGB(x, y, source.getRGB(x, y));
            }
        }
    }

    default <T extends Image> T convert(Builder<T> builder) {
        T image = builder.build(getWidth(), getHeight());
        image.copyPixels(this);
        return image;
    }

    interface Builder<T extends Image> {
        T build(int width, int height);
    }

}
