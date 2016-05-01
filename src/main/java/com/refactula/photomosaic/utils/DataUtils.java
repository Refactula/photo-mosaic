package com.refactula.photomosaic.utils;

public class DataUtils {

    public static int unsignedByte(byte value) {
        return value >= 0 ? value : 256 + value;
    }

}
