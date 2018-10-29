package com.dudar.imagemedianfilter;

public class RGBImage {
    private int height;
    private int width;

    int[][] alpha;
    int[][] red;
    int[][] green;
    int[][] blue;

    public RGBImage(int width, int height) {
        this.width = width;
        this.height = height;

        red = new int[width][height];
        green = new int[width][height];
        blue = new int[width][height];
        alpha = new int[width][height];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
