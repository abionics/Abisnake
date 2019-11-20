package io.battlesnake.starter.help;

import java.util.Objects;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point point) {
        x = point.x;
        y = point.y;
    }

    public static Point add(Point first, Point second) {
        return new Point(first.x + second.x, first.y + second.y);
    }

    public static int dist(Point first, Point second) {
        return Math.abs(first.x - second.x) + Math.abs(first.y - second.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x) ^ Objects.hash(y);
    }

    @Override
    public String toString() {
        return "[" + x + ";" + y + "]";
    }
}

