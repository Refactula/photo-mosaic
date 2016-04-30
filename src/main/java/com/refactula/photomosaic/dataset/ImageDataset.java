package com.refactula.photomosaic.dataset;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface ImageDataset {

    InputStream openInputStream(int fromIndex) throws IOException;

    default DataInputStream openDataInputStream(int fromIndex) throws IOException {
        return new DataInputStream(new BufferedInputStream(openInputStream(fromIndex)));
    }

}
