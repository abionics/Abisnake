package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;

import java.util.*;

public class Directions {
    private final static Map<String, Point> directions = new HashMap<String, Point>() {{
        put("left", new Point(-1, 0));
        put("right", new Point(+1, 0));
        put("up", new Point(0, -1));
        put("down", new Point(0, +1));
    }};

    public static ArrayList<String> names() {
        return new ArrayList<>(directions.keySet());
    }

    public static ArrayList<Point> values() {
        return new ArrayList<>(directions.values());
    }
}
