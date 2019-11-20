package io.battlesnake.starter.model.prediction;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.help.Printer;
import io.battlesnake.starter.model.Core;
import io.battlesnake.starter.model.heuristic.Heuristic;

import java.util.Arrays;

public class Prediction {
    final static Point[] directions = new Point[]{
            new Point(-1, 0),
            new Point(+1, 0),
            new Point(0, -1),
            new Point(0, +1),
    };
    final static String[] names = new String[]{"right", "left", "down", "up"};

    public String predict(Core core) {
        System.out.println(core.snakes.size());
        int deep = 3;  //odd (непарное епта) 2*n+1
        int[] scores = new int[4];
        PredictionItem current = new PredictionItem(core);
        System.out.println(current.snakes.size());
        for (int directionID = 0; directionID < 4; directionID++) {
            PredictionItem next = new PredictionItem(current);
            int result = next.moveMe(directionID);
            if (result > 0) {
                int val = predict(next, deep);
                scores[directionID] = val;
            }
        }
        System.out.println(Arrays.toString(scores));
        int maxi = 0;
        for (int i = 0; i < 4; i++) {
            if (scores[i] > scores[maxi]) maxi = i;
        }
        System.out.println(names[maxi]);
        return names[maxi];
    }

    private int predict(PredictionItem current, int deep) {
        if (deep == 0) {
            Printer.print(current.field, current.height);
            System.out.println();
            Heuristic heuristic = new Heuristic(current);
            return heuristic.heuristic().getValue();
        } else {
            deep--;
            if (deep % 2 == 0) {
                //move other
                int min = Integer.MAX_VALUE;
                for (int snakeID = 0; snakeID < current.snakes.size(); snakeID++)
                    for (int directionID = 0; directionID < 4; directionID++) {
                        PredictionItem next = new PredictionItem(current);
                        int result = next.moveOther(snakeID, directionID);
                        if (result > 0) {
                            next.init();
                            int val = predict(next, deep);
                            if (val < min) min = val;
                        }
                    }
                System.out.println(deep + " (other) : " + min);
                return min;
            } else {
                //move me
                int max = Integer.MIN_VALUE;
                for (int directionID = 0; directionID < 4; directionID++) {
                    PredictionItem next = new PredictionItem(current);
                    int result = next.moveMe(directionID);
                    if (result > 0) {
                        next.init();
                        int val = predict(next, deep);
                        if (val > max) max = val;
                    }
                }
                System.out.println(deep + " (me) : " + max);
                return max;
            }
        }
    }
}
