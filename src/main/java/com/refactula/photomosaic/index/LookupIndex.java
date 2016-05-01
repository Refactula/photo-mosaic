package com.refactula.photomosaic.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LookupIndex {

    private final ArrayList<Integer> order;
    private final ArrayList<Integer> reds;
    private final ArrayList<Integer> greens;
    private final ArrayList<Integer> blues;

    public LookupIndex(int maxDistance) {
        order = new ArrayList<>();
        reds = new ArrayList<>();
        greens = new ArrayList<>();
        blues = new ArrayList<>();

        for (int r = -maxDistance; r <= maxDistance; r++) {
            for (int g = -maxDistance; g <= maxDistance; g++) {
                for (int b = -maxDistance; b <= maxDistance; b++) {
                    int sum = Math.abs(r) + Math.abs(g) + Math.abs(b);
                    if (sum <= maxDistance) {
                        reds.add(r);
                        greens.add(g);
                        blues.add(b);
                        order.add(order.size());
                    }
                }
            }
        }

        Collections.sort(order, (i, j) -> Integer.compare(
                Math.abs(reds.get(i)) + Math.abs(greens.get(i)) + Math.abs(blues.get(i)),
                Math.abs(reds.get(j)) + Math.abs(greens.get(j)) + Math.abs(blues.get(j))
        ));
    }

    public int size() {
        return order.size();
    }

    public int getRed(int i) {
        return reds.get(order.get(i));
    }

    public int getGreen(int i) {
        return greens.get(order.get(i));
    }

    public int getBlue(int i) {
        return blues.get(order.get(i));
    }

}
