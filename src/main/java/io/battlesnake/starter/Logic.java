package io.battlesnake.starter;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class Logic {
    private static final String NAME = "StarterSnake";

    private final int width;
    private final int height;
    private final ArrayList<Point> food;
    private final ArrayList<Snake> snakes;
    private final Snake me;

    private Element[][] field;


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
            field[x][y] = Element.BODY;
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
        snakes.remove(me);

        System.out.println(me.toString() + "\n");
        for (Snake snake : snakes)
            System.out.println("@ " + snake);
        for (Point point : food)
            System.out.println("* " + point);

        print();
    }

    void print() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                System.out.print(field[i][j] + "\t");
            System.out.println();
        }
    }
}
