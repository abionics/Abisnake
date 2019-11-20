package io.battlesnake.starter;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Snake {
    final String id;
    final String name;
    final int health;
    final ArrayList<Point> body;
    final Point head;

    Snake(JSONObject json) {
        id = json.getString("id");
        name = json.getString("name");
        health = json.getInt("health");
        body = new ArrayList<>();
        JSONArray bodyJSON = json.getJSONArray("body");
        for (int i = 0; i < bodyJSON.length(); i++) {
            JSONObject point = bodyJSON.getJSONObject(i);
            int x = point.getInt("x");
            int y = point.getInt("y");
            body.add(new Point(x, y));
        }
        head = body.get(0);
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Snake)) return false;
        Snake snake = (Snake) other;
        return snake.name.equals(this.name);
    }

    @Override
    public String toString() {
        return name + " (" + id + ") - " + health + " : " + body.toString();
    }
}
