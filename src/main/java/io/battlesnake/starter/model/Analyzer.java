package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.model.heuristic.Heuristic;
import io.battlesnake.starter.model.prediction.Prediction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Analyzer extends Core {
    boolean[] suitDirections = new boolean[4];
    boolean[][] visited;
    int countFree;

    public Analyzer(String json) {
        super(json);
    }

    public String analyze() {
        int suitCount = 4;
        for (int directionID = 0; directionID < 4; directionID++) {
            Point direction = Prediction.directions[directionID];
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

        for (int directionID = 0; directionID < 4; directionID++)
            if (suitDirections[directionID]) {
                visited = new boolean[width][height];
                countFree = 0;
                Point start = Point.add(head, Prediction.directions[directionID]);
                lookFree(start);
                if (countFree < me.length + 2 && suitCount > 0) {
                    suitDirections[directionID] = false;
                    suitCount--;
                }
                System.out.println(directionID + " " + countFree + " : " + suitDirections[directionID]);
            }
        if (suitCount == 1) return getSingleSuit();

        for (int directionID = 0; directionID < 4; directionID++)
            if (suitDirections[directionID]) {
                for (Snake snake : snakes) {
                    int mdist = Point.mdist(snake.head(), head);
                    if (mdist == 1 && snake.length > me.length && suitCount > 0) {
                        System.out.println("Snake " + snake.name + " is too close and bigger than you");
                        suitDirections[directionID] = false;
                        suitCount--;
                    }
                }
            }
        if (suitCount == 1) return getSingleSuit();

        ArrayList<Point> foods = new ArrayList<>(food.size());
        for (Point point : food) {
            foods.add(new Point(point));
        }
        foods.sort((o1, o2) -> -Point.dist(o1, o2));
        System.out.println(foods);

        int distToNearestFood = Point.dist(head, foods.get(0));
        double k = (me.health - distToNearestFood * 10.0) / me.health;
        System.out.println(k);
        if (k < 0) k = 0;
        if (k > 1) k = 1;

        k += 0.2; // hardcode value :D have fun^
        System.out.println(k);

        Heuristic heuristic = new Heuristic(this);
        heuristic.heuristic(k);
        HashMap<String, Integer> result = heuristic.get();

        for (int directionID = 0; directionID < 4; directionID++)
            if (!suitDirections[directionID])
                result.put(Prediction.names[directionID], Integer.MIN_VALUE);

        return heuristic.getResult().getKey();
    }

    private void lookFree(Point current) {
        for (Point direction : Prediction.directions) {
            Point next = Point.add(current, direction);
            if (get(next.x, next.y) == Element.NONE && !visited[next.x][next.y]) {
                visited[next.x][next.y] = true;
                countFree++;
                lookFree(next);
            }
        }
    }

    private String getSingleSuit() {
        for (int i = 0; i < 4; i++)
            if (suitDirections[i])
                return Prediction.names[i];
        return Prediction.names[0];
    }
}
