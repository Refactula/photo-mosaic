package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.index.AverageColorIndex;

import java.io.IOException;

public class DevelopmentApp {

    public static void main(String[] args) throws Exception {
        ImageDataset dataset = loadDataset();
        AverageColorIndex index = AverageColorIndex.readFromFile("index.bin", dataset.size());

    }

    private static ImageDataset loadDataset() throws IOException {
        ImageDataset dataset;
        try (ImageDataset fileDataset = FileDataset.forFile("dataset.bin", 100000, 16, 16)) {
            dataset = InMemoryDataset.copyOf(fileDataset);
        }
        return dataset;
    }

}
