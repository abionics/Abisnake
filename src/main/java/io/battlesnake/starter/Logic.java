package io.battlesnake.starter;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

class Logic {
    private static final int NEAREST_FIELD_SIZE = 7;
    private static final int NEAREST_FIELD_CENTER = NEAREST_FIELD_SIZE / 2;

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

    String heuristic() {
        Element[][] nearest = getNearest();
        int[][] foodWeight = readFile("food.txt");
        int[][] bodyWeight = readFile("body.txt");
        print(NEAREST_FIELD_SIZE);

        HashMap<String, Integer> directions = new HashMap<>(4);
        directions.put("up", 0);
        directions.put("right", 0);
        directions.put("down", 0);
        directions.put("left", 0);
        BiConsumer<String, Integer> sum = (direction, value) -> {
            int val = directions.get(direction);
            directions.put(direction, value + val);
        };
        BiConsumer<Point, Integer> calculate = (point, value) -> {
            if (point.x < NEAREST_FIELD_CENTER) sum.accept("left", value);
            if (point.x > NEAREST_FIELD_CENTER) sum.accept("right", value);
            if (point.y > NEAREST_FIELD_CENTER) sum.accept("down", value);
            if (point.y < NEAREST_FIELD_CENTER) sum.accept("up", value);
        };

        for (int i = 0; i < NEAREST_FIELD_SIZE; i++)
            for (int j = 0; j < NEAREST_FIELD_SIZE; j++) {
                Element element = nearest[i][j];
                Point point = new Point(i, j);
                if (element == Element.FOOD) calculate.accept(point, foodWeight[i][j]);
                if (element == Element.BODY) calculate.accept(point, bodyWeight[i][j]);
            }

        print(nearest, NEAREST_FIELD_SIZE);
        Map.Entry<String, Integer> max = null;
        for (Map.Entry<String, Integer> item : directions.entrySet()) {
            if (max == null || item.getValue() > max.getValue()) {
                max = item;
            }
        }
        System.out.println(directions);
        System.out.println(max);
        return max.getKey();
    }

    private int[][] readFile(String name) {
        try {
            int[][] result = new int[NEAREST_FIELD_SIZE][NEAREST_FIELD_SIZE];
            for (int i = 0; i < NEAREST_FIELD_SIZE; i++)
                for (int j = 0; j < NEAREST_FIELD_SIZE; j++)
                    result[i][j] = 0;
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
            return new int[0][0];
        }
    }

    private Element[][] getNearest() {
        Element[][] result = new Element[NEAREST_FIELD_SIZE][NEAREST_FIELD_SIZE];
        for (int i = 0; i < NEAREST_FIELD_SIZE; i++)
            for (int j = 0; j < NEAREST_FIELD_SIZE; j++)
                result[i][j] = get(head.x + i - NEAREST_FIELD_CENTER, head.y + j - NEAREST_FIELD_CENTER);
        return result;
    }

    private Element get(int x, int y) {
        if (x == -1 || x == width || y == -1 || y == height) return Element.BODY;
        if (x < 0 || x >= width || y < 0 || y >= height) return Element.NONE;
        return field[x][y];
    }

    
    private <T> void print(T[][] array, int side) {
        for (int j = 0; j < side; j++) {
            for (int i = 0; i < side; i++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }

    private void print(int side) {
        for (int j = 0; j < side; j++) {
            for (int i = 0; i < side; i++)
                System.out.print(new Point(i, j) + "\t");
            System.out.println();
        }
    }

    private void print(int[][] array, int side) {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++)
                System.out.print(array[i][j] + "\t");
            System.out.println();
        }
    }
}
