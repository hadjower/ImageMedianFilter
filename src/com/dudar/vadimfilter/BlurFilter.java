package com.dudar.vadimfilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlurFilter {

    private BufferedImage source;

    public BlurFilter(BufferedImage source) {
        this.source = source;
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    private Color blurPixel(int x, int y, int radius) {
        final Point firstPixel = new Point(
                clamp(x - radius - 1, 0, source.getWidth()),
                clamp(y - radius - 1, 0, source.getHeight()));
        final Point lastPixel = new Point(
                clamp(x + radius + 1, 0, source.getWidth()),
                clamp(y + radius + 1, 0, source.getHeight()));

        int redAcc = 0;
        int greenAcc = 0;
        int blueAcc = 0;
        int alphaAcc = 0;
        int pixels = 0;

        for (int i = (int) firstPixel.getX(); i < lastPixel.getX(); i++) {
            for (int j = (int) firstPixel.getY(); j < lastPixel.getY(); j++) {
                final Color color = new Color(source.getRGB(i, j));
                redAcc += color.getRed();
                greenAcc += color.getGreen();
                blueAcc += color.getBlue();
                alphaAcc += color.getAlpha();
                pixels += 1;
            }
        }
        return new Color(
                redAcc / pixels,
                greenAcc / pixels,
                blueAcc / pixels,
                alphaAcc / pixels);
    }

    private BufferedImage blur0(BufferedImage result, int radius, int from, int to) {
        for (int i = from; i < to; i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                Color blurredPixel = blurPixel(i, j, radius);
                result.setRGB(i, j, blurredPixel.getRGB());
            }
        }
        return result;
    }

    public BufferedImage blur(int radius) {
        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        return blur0(result, radius,0, source.getWidth());
    }

    public BufferedImage concurrentBlur(int radius) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        int cores = Runtime.getRuntime().availableProcessors();
        int step = source.getWidth() / cores;
        int currentStep = 0;
        for (int i = 0; i < cores; i++) {
            int from = currentStep;
            int to;
            if (i == cores - 1) {
                to = source.getWidth();
            } else {
                to = currentStep + step;
            }
            Thread thread = new Thread(() -> blur0(result, radius, from, to));
            threads.add(thread);
            thread.start();
            currentStep += step;
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return result;
    }
}
