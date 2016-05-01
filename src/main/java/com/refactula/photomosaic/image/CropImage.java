package com.refactula.photomosaic.image;

public class CropImage implements Image {

    private final Image delegate;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public CropImage(Image delegate, int offsetX, int offsetY, int width, int height) {
        this.delegate = delegate;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = Math.min(width, delegate.getWidth() - offsetX);
        this.height = Math.min(height, delegate.getHeight() - offsetY);
    }

    private int mapX(int x) {
        if (x < 0 || x >= width) {
            throw new IndexOutOfBoundsException();
        }
        return x + offsetX;
    }

    private int mapY(int y) {
        if (y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }
        return y + offsetY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getRGB(int x, int y) {
        return delegate.getRGB(mapX(x), mapY(y));
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
        delegate.setRGB(mapX(x), mapY(y), rgb);
    }

    @Override
    public int get(ColorChannel colorChannel, int x, int y) {
        return delegate.get(colorChannel, mapX(x), mapY(y));
    }

    @Override
    public void set(ColorChannel colorChannel, int x, int y, int value) {
        delegate.set(colorChannel, mapX(x), mapY(y), value);
    }

}
