package com.refactula.photomosaic.image;

import java.util.Arrays;

public class AverageColor {

    private final long[] sums = new long[ColorChannel.values().length];
    private final int[] count = new int[ColorChannel.values().length];

    public void compute(Image image) {
        clear();
        for (ColorChannel channel : ColorChannel.values()) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    add(channel, image.get(channel, x, y));
                }
            }
        }
    }

    public void add(ColorChannel channel, int value) {
        sums[channel.ordinal()] += value;
        count[channel.ordinal()]++;
    }

    public void clear() {
        Arrays.fill(sums, 0);
        Arrays.fill(count, 0);
    }

    public int get(ColorChannel channel) {
        return (int) (sums[channel.ordinal()] / count[channel.ordinal()]);
    }

    public int getRGB() {
        return ColorChannel.toRGB(get(ColorChannel.RED), get(ColorChannel.GREEN), get(ColorChannel.BLUE));
    }
}
