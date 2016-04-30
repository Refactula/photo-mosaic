package com.refactula.photomosaic.image;

public enum ColorChannel {
    RED(0x10),
    GREEN(0x8),
    BLUE(0x0);

    public final int bitShift;

    ColorChannel(int bitShift) {
        this.bitShift = bitShift;
    }

    public static int toRGB(int red, int green, int blue) {
        return red << RED.bitShift | green << GREEN.bitShift | blue << BLUE.bitShift;
    }

    public int extract(int rgb) {
        return rgb >> bitShift & 0xff;
    }
}
