package io.battlesnake.starter;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logic {
    final int height;
    final int width;
    final ArrayList<Point> food;
    final ArrayList<Snake> snakes;
    final Snake me;

    Logic(String json) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));
        writer.write(json);

        JSONObject object = new JSONObject(json);

        JSONObject board = object.getJSONObject("board");
        height = board.getInt("height");
        width = board.getInt("width");
        food = new ArrayList<>();
        JSONArray food = board.getJSONArray("food");
        for (int i = 0; i < food.length(); i++) {
            JSONObject point = food.getJSONObject(i);
            int x = point.getInt("x");
            int y = point.getInt("y");
            this.food.add(new Point(x, y));
        }
        snakes = new ArrayList<>();
        JSONArray snakes = board.getJSONArray("snakes");
        for (int i = 0; i < food.length(); i++) {
            Snake snake = new Snake(snakes.getJSONObject(i));
            this.snakes.add(snake);
        }

        me = new Snake(object.getJSONObject("you"));
        writer.write(me.toString());
        writer.close();
    }
}
