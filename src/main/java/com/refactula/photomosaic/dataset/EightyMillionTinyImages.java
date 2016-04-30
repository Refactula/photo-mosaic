package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.ColorChannel;
import com.refactula.photomosaic.image.Image;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class EightyMillionTinyImages implements ImageDataset {

    private static final String DATASET_URL = "http://horatio.cs.nyu.edu/mit/tiny/data/tiny_images.bin";

    public static final int IMAGE_WIDTH = 32;
    public static final int IMAGE_HEIGHT = 32;

    @Override
    public Iterator<Image> iterator(int fromIndex) {
        return new MyIterator(fromIndex);
    }

    private static class MyIterator implements Iterator<Image> {
        private final ArrayImage arrayImage;
        private final DataInputStream input;
        private boolean isPending = false;
        private boolean isEnded = false;

        public MyIterator(int fromIndex) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(DATASET_URL).openConnection();
                long bytesOffset = (long) fromIndex * IMAGE_WIDTH * IMAGE_HEIGHT * ColorChannel.values().length;
                connection.setRequestProperty("Range", "bytes=" + bytesOffset + "-");
                connection.connect();

                if (connection.getResponseCode() == 416) {
                    isEnded = true;
                    input = null;
                } else {
                    input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                }

                arrayImage = new ArrayImage(IMAGE_WIDTH, IMAGE_HEIGHT);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            read();
            return isPending;
        }

        private void read() {
            if (isPending || isEnded) {
                return;
            }
            try {
                arrayImage.readFrom(input);
                isPending = true;
            } catch (EOFException e) {
                isEnded = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Image next() {
            read();
            if (!isPending) {
                throw new NoSuchElementException();
            }
            isPending = false;
            return arrayImage;
        }

    }

}
