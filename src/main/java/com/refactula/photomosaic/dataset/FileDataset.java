package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.ColorChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDataset extends StreamImageDataset {

    private final int imageSizeBytes;
    private final RandomAccessFile file;

    public FileDataset(RandomAccessFile file, int size,  int imageWidth, int imageHeight) {
        super(size, imageWidth, imageHeight);
        this.file = file;
        this.imageSizeBytes = ColorChannel.values().length * imageWidth * imageHeight;
    }

    public static FileDataset forFile(String fileName, int size, int imageWidth, int imageHeight) throws FileNotFoundException {
        return forFile(new File(fileName), size, imageWidth, imageHeight);
    }

    public static FileDataset forFile(File file, int size, int imageWidth, int imageHeight) throws FileNotFoundException {
        return new FileDataset(new RandomAccessFile(file, "r"), size, imageWidth, imageHeight);
    }

    @Override
    protected void changePosition(int index) throws IOException {
        file.seek(index * imageSizeBytes);
    }

    @Override
    protected void readTo(ArrayImage destination) throws IOException {
        destination.readFrom(file);
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
