package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Core {
    private final int width;
    private final int height;
    private final ArrayList<Point> food;
    private final ArrayList<Snake> snakes;
    private final Snake me;

    private Element[][] field;
    private Point head;


    public Core(String json) {
        JSONObject object = new JSONObject(json);

        JSONObject board = object.getJSONObject("board");
        width = board.getInt("width");
        height = board.getInt("height");

        food = new ArrayList<>();
        JSONArray foodJSON = board.getJSONArray("food");
        for (int i = 0; i < foodJSON.length(); i++) {
            JSONObject point = foodJSON.getJSONObject(i);
            int x = point.getInt("x");
            int y = point.getInt("y");
            food.add(new Point(x, y));
        }
        snakes = new ArrayList<>();
        JSONArray snakesJSON = board.getJSONArray("snakes");
        for (int i = 0; i < snakesJSON.length(); i++) {
            Snake snake = new Snake(snakesJSON.getJSONObject(i));
            snakes.add(snake);
        }

        me = new Snake(object.getJSONObject("you"));
        head = me.head();
        snakes.remove(me);

        System.out.println("! " + me.toString());
        for (Snake snake : snakes)
            System.out.println("@ " + snake);
        for (Point point : food)
            System.out.println("* " + point);

        init();
    }

    public void init() {
        field = new Element[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                field[i][j] = Element.NONE;
        for (Point point : food)
            field[point.x][point.y] = Element.FOOD;
        for (Snake snake : snakes) {
            for (Point point : snake.body)
                field[point.x][point.y] = Element.BODY;
            Point head = snake.head();
            field[head.x][head.y] = Element.HEAD;
        }
        System.out.println(me.body);
        for (Point point : me.body)
            field[point.x][point.y] = Element.BODY;
    }

    public Element[][] getNearest(int size) {
        int middle = size / 2;
        Element[][] result = new Element[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = get(head.x + i - middle, head.y + j - middle);
        return result;
    }

    private Element get(int x, int y) {
        if (x == -1 || x == width || y == -1 || y == height) return Element.BODY;
        if (x < -1 || x > width || y < -1 || y > height) return Element.NONE;
        return field[x][y];
    }
}
