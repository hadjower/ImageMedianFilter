package com.dudar.imagemedianfilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ImageConverter {
    public RGBImage imageToRGB(BufferedImage image) {
        Instant start = Instant.now();

        final int height = image.getHeight();
        final int width = image.getWidth();

        final RGBImage rgbImage = new RGBImage(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);

                rgbImage.alpha[i][j] = (pixel >> 24) & 0xff; // alpha
                rgbImage.red[i][j] = (pixel >> 16) & 0xff; // red
                rgbImage.green[i][j] = (pixel >> 8) & 0xff; // green
                rgbImage.blue[i][j] = pixel & 0xff; // blue
            }
        }

        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.format("Image to rgb: %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds() % 60, between.toMillis());

        return rgbImage;
    }

    public BufferedImage rgbToImage(RGBImage rgbImage, int type) {
        Instant start = Instant.now();

        BufferedImage image = new BufferedImage(
                rgbImage.getWidth(),
                rgbImage.getHeight(),
                type
        );
        for (int i = 0; i < rgbImage.getWidth(); i++) {
            for (int j = 0; j < rgbImage.getHeight(); j++) {
                int alpha = rgbImage.alpha[i][j];
                int red = rgbImage.red[i][j];
                int green = rgbImage.green[i][j];
                int blue = rgbImage.blue[i][j];

                image.setRGB(i, j, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }

        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.format("RGB to image: %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds(), between.toMillis());

        return image;
    }

    public RGBImage filter(RGBImage rgbImage, Filter filter) {
        RGBImage result = new RGBImage(rgbImage.getWidth(), rgbImage.getHeight());
        Instant start = Instant.now();
        result.alpha = filter.filter(rgbImage.alpha);
        result.red = filter.filter(rgbImage.red);
        result.green = filter.filter(rgbImage.green);
        result.blue = filter.filter(rgbImage.blue);
        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.format("Simple filter: %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds() % 60, between.toMillis());
        return result;
    }

    public RGBImage filterConcurrently(RGBImage rgbImage, Filter filter) {
        final RGBImage result = new RGBImage(rgbImage.getWidth(), rgbImage.getHeight());

        Instant start = Instant.now();

        final int cores = Runtime.getRuntime().availableProcessors();

        System.out.println("Starting " + cores + " threads");
        List<Thread> threads = new ArrayList<>(cores);

        final int lengthNormal = rgbImage.getWidth() / cores;
        final int lengthWithMode = lengthNormal + rgbImage.getWidth() % cores;
        for (int i = 0; i < cores; i++) {
            int startX = i * lengthNormal;
            int length = i == cores - 1 ? lengthWithMode : lengthNormal;

            Thread thread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " started, startX = " + startX + " Length = " + length);
                result.alpha = filter.filter(rgbImage.alpha, startX, length, result.alpha);
                result.red = filter.filter(rgbImage.red, startX, length, result.red);
                result.green = filter.filter(rgbImage.green, startX, length, result.green);
                result.blue = filter.filter(rgbImage.blue, startX, length, result.blue);
            }, "Thread_" + (i+1));

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.format("Concurrent filter: %02d:%02d.%04d \n", between.toMinutes(), between.getSeconds() % 60, between.toMillis());

        return result;
    }
}
