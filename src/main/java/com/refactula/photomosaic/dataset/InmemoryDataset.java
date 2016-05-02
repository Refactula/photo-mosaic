package com.refactula.photomosaic.dataset;

import com.google.common.collect.ImmutableList;
import com.refactula.photomosaic.image.AbstractImage;
import com.refactula.photomosaic.image.Image;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class InMemoryDataset extends AbstractImageDataset {

    private final List<Image> images;

    public InMemoryDataset(List<Image> images, int imageWidth, int imageHeight) {
        super(images.size(), imageWidth, imageHeight);
        this.images = images;
    }

    public static InMemoryDataset copyOf(ImageDataset dataset) throws IOException {
        ImmutableList.Builder<Image> listBuilder = ImmutableList.builder();
        for (int i = 0; i < dataset.size(); i++) {
            listBuilder.add(dataset.get(i));
        }
        return new InMemoryDataset(listBuilder.build(), dataset.getImageWidth(), dataset.getImageHeight());
    }

    public static InMemoryDataset randomSubsetOf(Random random, ImageDataset dataset, int size) throws IOException {
        ImmutableList.Builder<Image> listBuilder = ImmutableList.builder();
        for (int i = 0; i < size; i++) {
            listBuilder.add(dataset.get(random.nextInt(dataset.size())));
        }
        return new InMemoryDataset(listBuilder.build(), dataset.getImageWidth(), dataset.getImageHeight());
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public Image get(int index) throws IOException {
        return index < images.size() ? images.get(index) : null;
    }

}
