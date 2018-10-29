package com.dudar.imagemedianfilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public void saveImage(BufferedImage image, String path, String extension) throws IOException {
        File outputFile = new File(path);
        ImageIO.write(image, extension, outputFile);
    }
}
