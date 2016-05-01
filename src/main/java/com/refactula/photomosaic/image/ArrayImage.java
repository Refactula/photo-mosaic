package com.refactula.photomosaic.image;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
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

    public static ArrayImage createHalfSizeScale(ArrayImage image) {
        ArrayImage result = new ArrayImage(image.getWidth() / 2, image.getHeight() / 2);
        image.scaleHalfSize(result);
        return result;
    }

    public void scaleHalfSize(ArrayImage halfSizeImage) {
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

    public void readFrom(DataInput input) throws IOException {
        for (int colorChannel = 0; colorChannel < data.length; colorChannel++) {
            for (int x = 0; x < data[colorChannel].length; x++) {
                for (int y = 0; y < data[colorChannel][x].length; y++) {
                    data[colorChannel][x][y] = input.readUnsignedByte();
                }
            }
        }
    }

    public void writeTo(DataOutput output) throws IOException {
        for (int colorChannel = 0; colorChannel < data.length; colorChannel++) {
            for (int x = 0; x < data[colorChannel].length; x++) {
                for (int y = 0; y < data[colorChannel][x].length; y++) {
                    output.writeByte(data[colorChannel][x][y]);
                }
            }
        }
    }
}
