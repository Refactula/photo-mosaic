package com.refactula.photomosaic.image;

import com.refactula.photomosaic.utils.DataUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ArrayImage extends AbstractImage {

    private final byte[] data;
    private final int multiplierColorChannel;
    private final int multiplierX;
    private final int multiplierY;

    public ArrayImage(int width, int height) {
        super(width, height);
        this.data = new byte[ColorChannel.values().length * width * height];

        this.multiplierY = 1;
        this.multiplierX = height * multiplierY;
        this.multiplierColorChannel = width * multiplierX;
    }

    @Override
    public int get(ColorChannel channel, int x, int y) {
        return DataUtils.unsignedByte(data[channel.ordinal() * multiplierColorChannel + x * multiplierX + y * multiplierY]);
    }

    @Override
    public void set(ColorChannel channel, int x, int y, int value) {
        data[channel.ordinal() * multiplierColorChannel + x * multiplierX + y * multiplierY] = (byte) value;
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
        input.readFully(data);
    }

    public void writeTo(DataOutput output) throws IOException {
        output.write(data);
    }
}
