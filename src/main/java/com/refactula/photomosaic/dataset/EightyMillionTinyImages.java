package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ColorChannel;
import com.refactula.photomosaic.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class EightyMillionTinyImages implements ImageDataset {

    private static final String DATASET_URL = "http://horatio.cs.nyu.edu/mit/tiny/data/tiny_images.bin";

    public static final int IMAGE_WIDTH = 32;
    public static final int IMAGE_HEIGHT = 32;

    private static final int IMAGE_SIZE_BYTES = ColorChannel.values().length * IMAGE_WIDTH * IMAGE_HEIGHT;

    @Override
    public InputStream openInputStream(int fromIndex) throws IOException {
        return IOUtils.connectHttp(DATASET_URL, (long) fromIndex * IMAGE_SIZE_BYTES);
    }

}
