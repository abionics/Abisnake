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

    Snake(JSONObject json) {
        id = json.getString("id");
        name = json.getString("id");
        health = json.getInt("id");
        body = new ArrayList<>();
        JSONArray body = json.getJSONArray("body");
        for (int i = 0; i < body.length(); i++) {
            JSONObject point = body.getJSONObject(i);
            int x = point.getInt("x");
            int y = point.getInt("y");
            this.body.add(new Point(x, y));
        }
    }

    @Override
    public String toString() {
        return name + " (" + id + ") - " + health + " : " + body.toString();
    }
}
