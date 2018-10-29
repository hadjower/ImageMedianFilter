package com.dudar.imagemedianfilter;

public interface Filter {
    int[][] filter(int[][] matrix);
    int[][] filter(int[][] matrix, int startX, int length, int[][] result);
}
