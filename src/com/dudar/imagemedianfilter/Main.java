package com.dudar.imagemedianfilter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static final String PATH = "C:/Users/Vlad/IdeaProjects/ImageMedianFilter/src/com/dudar/imagemedianfilter/";

    public static void main(String[] args) {


        ImageLoader imageLoader = new ImageLoader();
        ImageConverter imageConverter = new ImageConverter();
        try {
            BufferedImage original = imageLoader.loadImage(PATH + "pic.jpg");

            RGBImage rgbOriginal = imageConverter.imageToRGB(original);

            Filter filter = new MedianFilter(1);

            BufferedImage simpleFiltered = imageConverter.rgbToImage(
                    imageConverter.filter(rgbOriginal, filter),
                    original.getType()
            );
            imageLoader.saveImage(simpleFiltered, PATH + "simple_filtered.jpg", "jpg");

            BufferedImage concurrentFiltered = imageConverter.rgbToImage(
                    imageConverter.filterConcurrently(rgbOriginal, filter),
                    original.getType()
            );
            imageLoader.saveImage(concurrentFiltered, PATH + "concurrent_filtered.jpg", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        int[][] array = {{2, 7, 4}, {2, 7, 4}, {2, 7, 4}, {2, 7, 4}, {2, 7, 4}};
        int[][] toCopy = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};

        int staryX = 1;
        int step = 3;

        System.arraycopy(toCopy, 0, array, staryX, step);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void test() {
        Runnable runnable = () -> {
            System.out.println("Thread started:::" + Thread.currentThread().getName());
            try {
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " step " + (i + 1));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread ended:::" + Thread.currentThread().getName());
        };

        System.out.println("Main started");
        Thread thread1 = new Thread(runnable, "Thread_1");
        Thread thread2 = new Thread(runnable, "Thread_2");
        Thread thread3 = new Thread(runnable, "Thread_3");
        Thread[] threads = {thread1, thread2, thread3};

        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();

            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main finished");
    }
}
