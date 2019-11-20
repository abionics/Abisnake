package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Snake {
    final String id;
    final String name;
    final int health;
    final ArrayList<Point> body;
    final int length;

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
        length = body.size();
    }

    Point head() {
        return body.get(0);
    }

    void move(int dx, int dy, boolean isEaten) {
        Point last = new Point(body.get(body.size() - 1));
        for (int i = body.size() - 1; i > 0; i++) {
            body.set(i, body.get(i - 1));
        }
        if (isEaten) body.add(last);
        head().x += dx;
        head().y += dy;
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
