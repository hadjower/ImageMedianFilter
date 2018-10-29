package com.dudar.vadimfilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {
    public static final String PATH = "C:/Users/Vlad/IdeaProjects/ImageMedianFilter/src/com/dudar/vadimfilter/";

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage image = ImageIO.read(new File(PATH + "pic.jpg"));
        BlurFilter blurFilter = new BlurFilter(image);
        double start = System.currentTimeMillis();
        BufferedImage blurred1 = blurFilter.concurrentBlur(5);
        double end = System.currentTimeMillis();
        System.out.println("Concurrent blur: " + ((end - start) / 1000) + "s");
//        start = System.currentTimeMillis();
//        BufferedImage blurred2 = blurFilter.blur(5);
//        end = System.currentTimeMillis();
//        System.out.println("Simple blur: " + ((end - start) / 1000) + "s");
        ImageIO.write(blurred1, "jpg", new File(PATH + "result1.jpg"));
//        ImageIO.write(blurred2, "jpg", new File(PATH + "result2.jpg"));
    }
}
