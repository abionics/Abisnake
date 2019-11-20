package io.battlesnake.starter.model;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.model.prediction.Prediction;

public class Analyzer extends Core {
    boolean[] suitDirections = new boolean[4];

    public Analyzer(String json) {
        super(json);
    }

//    public void analyze() {
//        for (int directionID = 0; directionID < 4; directionID++) {
//            Point direction = Prediction.directions[directionID];
//            Point nextHeadPoint = Point.add(me.head(), direction);
//            Element element = get(nextHeadPoint.x, nextHeadPoint.y);
//            switch (element) {
//                case WALL:
//                case BODY:
//                    suitDirections[directionID] = false;
//                    break;
//                    //
//
//                case WALL://NONE, HEAD, FOOD
//            }
//        }
//
//    }
}
