package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.Image;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EightyMillionTinyImages implements ImageDataset {

    private static final String DATASET_URL = "http://horatio.cs.nyu.edu/mit/tiny/data/tiny_images.bin";

    public static final int SIZE = 7527697;

    public static final int IMAGE_WIDTH = 32;
    public static final int IMAGE_HEIGHT = 32;

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public Iterator<Image> iterator(int fromIndex) {
        return new MyIterator(fromIndex);
    }

    private static class MyIterator implements Iterator<Image> {
        private int index;
        private final ArrayImage arrayImage;
        private final DataInputStream input;

        public MyIterator(int fromIndex) {
            if (fromIndex != 0) {
                throw new UnsupportedOperationException(); // TODO fix
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(DATASET_URL).openConnection();
                input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                arrayImage = new ArrayImage(IMAGE_WIDTH, IMAGE_HEIGHT);
                index = fromIndex;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return index + 1 < SIZE;
        }

        @Override
        public Image next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            try {
                index++;
                arrayImage.readFrom(input);
                return arrayImage;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
