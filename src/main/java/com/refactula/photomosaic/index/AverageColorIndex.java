package com.refactula.photomosaic.index;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AverageColorIndex {

    private final ResizeableIntArray[][][] tiles = new ResizeableIntArray[256][256][256];
    private final LookupIndex lookupIndex;
    private final ArrayList<Integer> candidates = new ArrayList<>();

    public AverageColorIndex(int maxSearchDistance) {
        this.lookupIndex = new LookupIndex(maxSearchDistance);
    }

    public void readFromFile(String fileName, int size) throws IOException {
        readFromFile(new File(fileName), size);
    }

    public void readFromFile(File file, int size) throws IOException {
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            for (int i = 0; i < size; i++) {
                int r = input.readUnsignedByte();
                int g = input.readUnsignedByte();
                int b = input.readUnsignedByte();
                ResizeableIntArray indexes = tiles[r][g][b];
                if (indexes == null) {
                    indexes = new ResizeableIntArray();
                    tiles[r][g][b] = indexes;
                }
                indexes.add(i);
            }
        }
    }

    public List<Integer> search(int r, int g, int b, int maxCandidates) {
        candidates.clear();
        for (int i = 0; i < lookupIndex.size(); i++) {
            int rr = r + lookupIndex.getRed(i);
            int gg = g + lookupIndex.getGreen(i);
            int bb = b + lookupIndex.getBlue(i);

            if (rr < 0 || rr >= 256 || gg < 0 || gg >= 256 || bb < 0 || bb >= 256) {
                continue;
            }

            ResizeableIntArray indexes = getIndexes(rr, gg, bb);
            for (int j = 0; j < indexes.size(); j++) {
                candidates.add(indexes.get(j));
                if (candidates.size() >= maxCandidates) {
                    return candidates;
                }
            }
        }
        return candidates;
    }

    private ResizeableIntArray getIndexes(int r, int g, int b) {
        return ResizeableIntArray.ofNullable(tiles[r][g][b]);
    }
}
