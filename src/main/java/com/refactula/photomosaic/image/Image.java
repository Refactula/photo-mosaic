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

    default int distance(Image other) {
        if (getWidth() != other.getWidth() || getHeight() != other.getHeight()) {
            throw new IllegalArgumentException("Images must have same size");
        }

        int distance = 0;
        for (ColorChannel channel : ColorChannel.values()) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    distance += Math.abs(get(channel, x, y) - other.get(channel, x, y));
                }
            }
        }

        return distance;
    }

    default void scaleHalfSize(ArrayImage halfSizeImage) {
        if (halfSizeImage.getWidth() * 2 != getWidth() || halfSizeImage.getHeight() * 2 != getHeight()) {
            throw new IllegalArgumentException();
        }

        for (int x = 0; x < halfSizeImage.getWidth(); x++) {
            for (int y = 0; y < halfSizeImage.getHeight(); y++) {
                for (ColorChannel channel : ColorChannel.values()) {
                    int sum = get(channel, 2 * x + 0, 2 * y + 0)
                            + get(channel, 2 * x + 0, 2 * y + 1)
                            + get(channel, 2 * x + 1, 2 * y + 0)
                            + get(channel, 2 * x + 1, 2 * y + 1);
                    halfSizeImage.set(channel, x, y, sum / 4);
                }
            }
        }
    }

    default void fill(int rgb) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setRGB(x, y, rgb);
            }
        }
    }

    interface Builder<T extends Image> {
        T build(int width, int height);
    }

}
