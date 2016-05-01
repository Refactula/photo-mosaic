package com.refactula.photomosaic.index;

import com.refactula.photomosaic.image.ColorChannel;
import com.refactula.photomosaic.utils.DataUtils;

import java.io.*;

public class AverageColorIndex {

    private final byte[] data;

    public AverageColorIndex(byte[] data) {
        this.data = data;
    }

    public static AverageColorIndex readFromFile(String fileName, int size) throws IOException {
        return readFromFile(new File(fileName), size);
    }

    public static AverageColorIndex readFromFile(File file, int size) throws IOException {
        byte[] data = new byte[ColorChannel.values().length * size];
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            input.readFully(data);
        }
        return new AverageColorIndex(data);
    }

    public int get(int index, ColorChannel channel) {
        return DataUtils.unsignedByte(data[index * ColorChannel.values().length + channel.ordinal()]);
    }

}
