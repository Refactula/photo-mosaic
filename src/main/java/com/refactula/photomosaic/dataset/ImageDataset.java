package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.Image;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ImageDataset {

    Iterator<Image> iterator(int fromIndex);

    default Stream<Image> stream(int fromIndex) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(fromIndex), Spliterator.ORDERED), false);
    }

}
