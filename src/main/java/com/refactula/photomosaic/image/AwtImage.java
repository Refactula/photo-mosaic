package com.refactula.photomosaic.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AwtImage extends AbstractImage {

    private static final ClassLoader CLASS_LOADER = AwtImage.class.getClassLoader();

    private final BufferedImage bufferedImage;

    public AwtImage(int width, int height) {
        super(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public AwtImage(BufferedImage bufferedImage) {
        super(bufferedImage.getWidth(), bufferedImage.getHeight());
        this.bufferedImage = bufferedImage;
    }

    public static AwtImage readFromResource(String resourceName) throws IOException {
        return new AwtImage(ImageIO.read(CLASS_LOADER.getResourceAsStream(resourceName)));
    }

    @Override
    public int getRGB(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
        bufferedImage.setRGB(x, y, rgb);
    }

    public void display() {
        display("Image");
    }

    public void display(String title) {
        JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(bufferedImage)), title, JOptionPane.PLAIN_MESSAGE, null);
    }

    public AwtImage scale(double scale) {
        int w = (int) (bufferedImage.getWidth() * scale);
        int h = (int) (bufferedImage.getHeight() * scale);
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return new AwtImage(scaleOp.filter(bufferedImage, after));
    }

}
