package com.refactula.photomosaic.image;

import com.refactula.photomosaic.utils.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AwtImage extends AbstractImage {

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
        try (InputStream input = IOUtils.connectResource(resourceName)) {
            return new AwtImage(ImageIO.read(input));
        }
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

    public void save(String format, File file) throws IOException {
        ImageIO.write(bufferedImage, format, file);
    }
}
