package com.dudar.imagemedianfilter;

import java.util.Arrays;

public class MedianFilter implements Filter {
    private int radius;

    public MedianFilter(int radius) {
        this.radius = radius;
    }

    @Override
    public int[][] filter(int[][] matrix) {
        int width = matrix.length;
        int height = matrix[0].length;
        return filter(matrix, 0, width, new int[width][height]);
    }

    @Override
    public int[][] filter(int[][] matrix, int startX, int length, int[][] result) {
        int width = matrix.length;
        int height = matrix[0].length;

        int localWidth = startX + length;
        int n = 2 * radius + 1;
        int medianIndex = n * n / 2;

        int[] localMatrix = new int[n * n];

        for (int i = startX; i < localWidth; i++) {
            for (int j = 0; j < height; j++) {

                for (int k = -radius; k <= radius; k++) {
                    for (int l = -radius; l <= radius; l++) {
                        int x = ((i + k) < 0) ? 0 : (((i + k) >= width) ? (width - 1) : (i + k));
                        int y = ((j + l) < 0) ? 0 : (((j + l) >= height) ? (height - 1) : (j + l));

                        int index = (k + radius) * n + (l + radius);

                        localMatrix[index] = matrix[x][y];
                    }
                }

                Arrays.sort(localMatrix);
                result[i][j] = localMatrix[medianIndex];
//                result[i][j] = mean(localMatrix);
            }
        }
        return result;
    }

    public int mean(int[] m) {
        int sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }
}
