package com.refactula.photomosaic.index;

import java.util.Arrays;

public class ResizeableIntArray {

    static final ResizeableIntArray EMPTY = new ResizeableIntArray() {
        @Override
        public void add(int value) {
            throw new RuntimeException("Trying to update the EMPTY instance");
        }
    };

    private static final int[] EMPTY_VALUES = new int[0];

    private int size = 0;
    private int[] values = EMPTY_VALUES;

    public static ResizeableIntArray ofNullable(ResizeableIntArray instance) {
        return instance == null ? EMPTY : instance;
    }

    public void add(int value) {
        ensureCapacity(size + 1);
        values[size++] = value;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > values.length) {
            values = Arrays.copyOf(values, Math.max(capacity, values.length * 2));
        }
    }

    public int size() {
        return size;
    }

    public int get(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return values[i];
    }

}
