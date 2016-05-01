package com.refactula.photomosaic.app;

import com.refactula.photomosaic.dataset.FileDataset;
import com.refactula.photomosaic.dataset.ImageDataset;
import com.refactula.photomosaic.dataset.InMemoryDataset;
import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.AverageColor;
import com.refactula.photomosaic.image.ColorChannel;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class BuildIndexApp {

    public static void main(String[] args) throws Exception {
        ImageDataset dataset;
        try (ImageDataset fileDataset = FileDataset.forFile("dataset.bin", 100000, 16, 16)) {
            dataset = InMemoryDataset.copyOf(fileDataset);
        }

        try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("index.bin")))) {
            ArrayImage buffer = dataset.createImageBuffer();
            AverageColor averageColor = new AverageColor();

            for (int i = 0; i < dataset.size(); i++) {
                if (!dataset.load(i, buffer)) {
                    throw new RuntimeException("Dataset size is invalid");
                }
                averageColor.compute(buffer);
                for (ColorChannel channel : ColorChannel.values()) {
                    output.writeByte(averageColor.get(channel));
                }
            }
        }
    }

}
