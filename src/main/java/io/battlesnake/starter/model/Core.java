package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Core {
    public final int width;
    public final int height;
    public final ArrayList<Point> food;
    public final ArrayList<Snake> snakes;
    public final Snake me;

    public Element[][] field;
    public Point head;


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
        snakes.remove(me);

        System.out.println("! " + me.toString());
        for (Snake snake : snakes)
            System.out.println("@ " + snake);
        for (Point point : food)
            System.out.println("* " + point);

        init();
    }

    public Core(Core other) {
        this.width = other.width;
        this.height = other.height;
        this.food = new ArrayList<>(other.food.size());
        for (Point point : other.food) {
            this.food.add(new Point(point));
        }
        this.snakes = new ArrayList<>(other.snakes.size());
        for (Snake snake : other.snakes) {
            this.snakes.add(new Snake(snake));
        }
        this.me = new Snake(other.me);

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
//        System.out.println(me.body);
        for (Point point : me.body)
            field[point.x][point.y] = Element.BODY;

        head = new Point(me.head());
    }

    public Element[][] getNearest(int size) {
        int middle = size / 2;
        Element[][] result = new Element[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = get(head.x + i - middle, head.y + j - middle);
        return result;
    }

    public Element get(int x, int y) {
        if (x == -1 || x == width || y == -1 || y == height) return Element.WALL;
        if (x < -1 || x > width || y < -1 || y > height) return Element.NONE;
        return field[x][y];
    }
}
