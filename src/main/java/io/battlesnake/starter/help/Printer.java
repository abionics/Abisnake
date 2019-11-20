package io.battlesnake.starter.help;

public class Printer {
    public static  <T> void print(T[][] array, int side) {
        for (int j = 0; j < side; j++) {
            for (int i = 0; i < side; i++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }

    public static void print(int side) {
        for (int j = 0; j < side; j++) {
            for (int i = 0; i < side; i++)
                System.out.print(new Point(i, j) + "\t");
            System.out.println();
        }
    }

    public static void print(int[][] array, int side) {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }
}
