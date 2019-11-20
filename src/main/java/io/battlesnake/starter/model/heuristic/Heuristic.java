package io.battlesnake.starter.model.heuristic;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.model.Core;
import io.battlesnake.starter.model.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Heuristic {
    private static final int NEAREST_FIELD_SIZE = 7;

    private Core core;
    private HashMap<String, Integer> directions;


    public Heuristic(Core core) {
        this.core = core;
    }

    public void heuristic(double hungry) {
        Element[][] nearest = core.getNearest(NEAREST_FIELD_SIZE);
        int[][] foodWeight = readFile("food.txt", NEAREST_FIELD_SIZE);
        int[][] bodyWeight = readFile("body.txt", NEAREST_FIELD_SIZE);
        int[][] headWeight = readFile("head.txt", NEAREST_FIELD_SIZE);
        int[][] wallWeight = readFile("wall.txt", NEAREST_FIELD_SIZE);

        directions = new HashMap<>(4);
        directions.put("up", 0);
        directions.put("right", 0);
        directions.put("down", 0);
        directions.put("left", 0);
        BiConsumer<String, Integer> sum = (direction, value) -> {
            int val = directions.get(direction);
            directions.put(direction, value + val);
        };
        BiConsumer<Point, Integer> calculate = (point, value) -> {
            int middle = NEAREST_FIELD_SIZE / 2;
            if (point.x < middle) sum.accept("left", value);
            if (point.x > middle) sum.accept("right", value);
            if (point.y > middle) sum.accept("down", value);
            if (point.y < middle) sum.accept("up", value);
        };

        for (int i = 0; i < NEAREST_FIELD_SIZE; i++)
            for (int j = 0; j < NEAREST_FIELD_SIZE; j++) {
                Element element = nearest[i][j];
                Point point = new Point(i, j);
                if (element == Element.FOOD) calculate.accept(point, (int) (foodWeight[i][j] * hungry));
                if (element == Element.BODY) calculate.accept(point, bodyWeight[i][j]);
                if (element == Element.HEAD) calculate.accept(point, headWeight[i][j]);
                if (element == Element.WALL) calculate.accept(point, wallWeight[i][j]);
            }

//        Printer.print(nearest, NEAREST_FIELD_SIZE);
    }

    public Map.Entry<String, Integer> getResult() {
        Map.Entry<String, Integer> max = null;
        for (Map.Entry<String, Integer> item : directions.entrySet()) {
            if (max == null || item.getValue() > max.getValue()) {
                max = item;
            }
        }
//        System.out.println(directions);
//        System.out.println(max);
        return max;
    }

    public HashMap<String, Integer> get() {
        return directions;
    }

    static int[][] readFile(String name, int size) {
        try {
            int[][] result = new int[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
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
}
