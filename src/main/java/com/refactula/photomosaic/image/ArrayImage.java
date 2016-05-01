package com.refactula.photomosaic.image;

import java.io.DataInput;
import java.io.IOException;

public class ArrayImage extends AbstractImage {

    private final int[][][] data;

    public ArrayImage(int width, int height) {
        super(width, height);
        data = new int[ColorChannel.values().length][width][height];
    }

    @Override
    public int get(ColorChannel colorChannel, int x, int y) {
        return data[colorChannel.ordinal()][x][y];
    }

    @Override
    public void set(ColorChannel colorChannel, int x, int y, int value) {
        data[colorChannel.ordinal()][x][y] = value;
    }

    public void readFrom(DataInput input) throws IOException {
        for (int colorChannel = 0; colorChannel < data.length; colorChannel++) {
            for (int x = 0; x < data[colorChannel].length; x++) {
                for (int y = 0; y < data[colorChannel][x].length; y++) {
                    data[colorChannel][x][y] = input.readUnsignedByte();
                }
            }
        }
    }

}
