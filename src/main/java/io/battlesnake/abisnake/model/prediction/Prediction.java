package io.battlesnake.abisnake.model.prediction;

import io.battlesnake.abisnake.help.Printer;
import io.battlesnake.abisnake.model.Core;
import io.battlesnake.abisnake.model.Directions;
import io.battlesnake.abisnake.model.heuristic.Heuristic;

import java.util.Arrays;

public class Prediction {
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
        String result = Directions.names().get(maxi);
        System.out.println(result);
        return result;
    }

    private int predict(PredictionItem current, int deep) {
        if (deep == 0) {
            Printer.print(current.field, current.height);
            System.out.println();
            Heuristic heuristic = new Heuristic(current);
            heuristic.heuristic(1);
            return heuristic.getResult().getValue();
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
