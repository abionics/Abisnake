package io.battlesnake.starter;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

class Logic {
    private static final String NAME = "StarterSnake";

    private final int width;
    private final int height;
    private final ArrayList<Point> food;
    private final ArrayList<Snake> snakes;
    private final Snake me;

    private Element[][] field;
    private Point head;


    Logic(String json) {
        System.out.println(json);
        JSONObject object = new JSONObject(json);

        JSONObject board = object.getJSONObject("board");
        width = board.getInt("width");
        height = board.getInt("height");
        field = new Element[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                field[i][j] = Element.NONE;

        food = new ArrayList<>();
        JSONArray foodJSON = board.getJSONArray("food");
        for (int i = 0; i < foodJSON.length(); i++) {
            JSONObject point = foodJSON.getJSONObject(i);
            int x = point.getInt("x");
            int y = point.getInt("y");
            food.add(new Point(x, y));
            field[x][y] = Element.FOOD;
        }
        snakes = new ArrayList<>();
        JSONArray snakesJSON = board.getJSONArray("snakes");
        for (int i = 0; i < snakesJSON.length(); i++) {
            Snake snake = new Snake(snakesJSON.getJSONObject(i));
            snakes.add(snake);
            for (Point point : snake.body)
                field[point.x][point.y] = Element.BODY;
        }

        me = new Snake(object.getJSONObject("you"));
        head = me.head;
        snakes.remove(me);

        System.out.println(me.toString() + "\n");
        for (Snake snake : snakes)
            System.out.println("@ " + snake);
        for (Point point : food)
            System.out.println("* " + point);

//        print();
    }

    void heuristic() {
        Element[][] nearest = getNearest();
        int[][] foodWeight = readFile("food.txt");
        int[][] bodyWeight = readFile("body.txt");
        print(nearest, 5, 5);
//        print(foodWeight, 5, 5);
    }

    private int[][] readFile(String name) {
        int size = 5;
        try {
            int[][] result = new int[size][size];
            BufferedReader reader = new BufferedReader(new FileReader(name));
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                int j = 0;
                String[] weights = line.split("\t");
                for (String weight : weights)
                    result[i][j++] = Integer.parseInt(weight);
                i++;
                line = reader.readLine();
            }
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Element[][] getNearest() {
        int size = 5;
        Element[][] result = new Element[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = get(head.x + i - 2, head.y + j - 2);
        return result;
    }

    private Element get(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return Element.NONE;
        return field[x][y];
    }

    private <T> void print(T[][] array, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }

    private void print(int[][] array, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }
}
