package com.refactula.photomosaic.image;

import java.awt.*;

public class AbstractImage implements Image {

    private final int width;
    private final int height;

    public AbstractImage(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getRGB(int x, int y) {
        int rgb = 0;
        for (ColorChannel channel : ColorChannel.values()) {
            rgb |= get(channel, x, y) << channel.bitShift;
        }
        return rgb;
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
        Color color = new Color(rgb);
        set(ColorChannel.RED, x, y, color.getRed());
        set(ColorChannel.GREEN, x, y, color.getGreen());
        set(ColorChannel.BLUE, x, y, color.getBlue());
    }

    @Override
    public int get(ColorChannel colorChannel, int x, int y) {
        return colorChannel.extract(getRGB(x, y));
    }

    @Override
    public void set(ColorChannel colorChannel, int x, int y, int value) {
        int rgb = 0;
        for (ColorChannel channel : ColorChannel.values()) {
            rgb |= (channel == colorChannel) ? value : get(channel, x, y);
        }
        setRGB(x, y, value);
    }

    public void copyPixels(Image source) {
        if (getWidth() != source.getWidth() || getHeight() != source.getHeight()) {
            throw new IllegalArgumentException("Images must have same size");
        }

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setRGB(x, y, source.getRGB(x, y));
            }
        }
    }

}
