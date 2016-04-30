package com.refactula.photomosaic.image;

public interface Image {

    int getWidth();

    int getHeight();

    /** Returns RGB value of given pixel */
    int getRGB(int x, int y);

    void setRGB(int x, int y, int rgb);

    /** Returns the color channel value of given pixel in range [0-256) */
    int get(ColorChannel colorChannel, int x, int y);

    void set(ColorChannel colorChannel, int x, int y, int value);

}
