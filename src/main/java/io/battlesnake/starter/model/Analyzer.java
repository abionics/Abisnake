package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.model.heuristic.Heuristic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Analyzer extends Core {
    private boolean[] suitDirections = new boolean[4];
    private boolean[][] visited;
    private int countFree;

    public Analyzer(String json) {
        super(json);
    }

    public String analyze() {
        int suitCount = 4;
        for (int directionID = 0; directionID < 4; directionID++) {
            Point direction = Directions.values().get(directionID);
            Point nextHeadPoint = Point.add(me.head(), direction);
            Element element = get(nextHeadPoint.x, nextHeadPoint.y);
            suitDirections[directionID] = true;
            switch (element) {
                case WALL:
                case BODY:
                    suitDirections[directionID] = false;
                    suitCount--;
                    break;
                    //

                //HEAD never, NONE, FOOD
            }
        }
        if (suitCount == 1) return getSingleSuit();
        System.out.println("l " + Arrays.toString(suitDirections));

        for (int directionID = 0; directionID < 4; directionID++)
            if (suitDirections[directionID]) {
                visited = new boolean[width][height];
                countFree = 0;
                Point start = Point.add(head, Directions.values().get(directionID));
                lookFree(start);
                if (countFree < me.length + 2 && suitCount > 1) {
                    suitDirections[directionID] = false;
                    suitCount--;
                }
                System.out.println(directionID + " " + countFree + " : " + suitDirections[directionID]);
            }
        if (suitCount == 1) return getSingleSuit();
        System.out.println("ll " + Arrays.toString(suitDirections));

        for (int directionID = 0; directionID < 4; directionID++)
            if (suitDirections[directionID]) {
                for (Snake snake : snakes) {
                    Point start = Point.add(head, Directions.values().get(directionID));
                    int dist = Point.dist(snake.head(), start);
                    System.out.println(dist + " " + head + " " + start + " | " + snake.length + " " + me.length);
                    if (dist == 1 && snake.length >= me.length && suitCount > 1) {
                        System.out.println("Snake " + snake.name + " is too close and bigger than you, remove " + directionID);
                        suitDirections[directionID] = false;
                        suitCount--;
                    }
                }
            }
        if (suitCount == 1) return getSingleSuit();
        System.out.println("lll " + Arrays.toString(suitDirections));

        ArrayList<Point> foods = new ArrayList<>(food.size());
        for (Point point : food) {
            foods.add(new Point(point));
        }
        foods.sort((o1, o2) -> -Point.dist(o1, o2));
        System.out.println(foods);

        int distNearestFood = 0;
        if (foods.size() != 0)
            distNearestFood = Point.dist(head, foods.get(0));
        double hungry = (me.health - distNearestFood * 10.0) / me.health;
        if (hungry < 0) hungry = 0;
        if (hungry > 1) hungry = 1;
        hungry += 0.2; // hardcode value :D have fun^
        System.out.println("hungry = " + hungry);

        Heuristic heuristic = new Heuristic(this);
        heuristic.heuristic(hungry);
        HashMap<String, Integer> result = heuristic.get();
        System.out.println(result);

        int max = Integer.MIN_VALUE;
        String res = "";
        Random rand = new Random();
        if (rand.nextInt(2) == 1) {
            for (int directionID = 0; directionID < 4; directionID++)
                if (suitDirections[directionID]) {
                    String name = Directions.names().get(directionID);
                    System.out.println(directionID + " " + name + result.get(name));
                    if (result.get(name) > max) {
                        max = result.get(name);
                        res = name;
                    }
                }
        } else {
            for (int directionID = 3; directionID >= 0; directionID--)
                if (suitDirections[directionID]) {
                    String name = Directions.names().get(directionID);
                    System.out.println(directionID + " " + name + result.get(name));
                    if (result.get(name) > max) {
                        max = result.get(name);
                        res = name;
                    }
                }
        }
        return res;
    }

    private void lookFree(Point current) {
        for (Point direction : Directions.values()) {
            Point next = Point.add(current, direction);
            if (get(next.x, next.y) == Element.NONE && !visited[next.x][next.y]) {
                visited[next.x][next.y] = true;
                countFree++;
                lookFree(next);
            }
        }
    }

    private String getSingleSuit() {
        for (int directionID = 0; directionID < 4; directionID++)
            if (suitDirections[directionID])
                return Directions.names().get(directionID);
        return Directions.names().get(0);   // never
    }
}
