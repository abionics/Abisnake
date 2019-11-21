package io.battlesnake.abisnake.model.prediction;

import io.battlesnake.abisnake.help.Point;
import io.battlesnake.abisnake.model.Core;
import io.battlesnake.abisnake.model.Directions;
import io.battlesnake.abisnake.model.Element;
import io.battlesnake.abisnake.model.Snake;

class PredictionItem extends Core {
    PredictionItem(Core core) {
        super(core);
    }

    int moveOther(int snakeID, int directionID) {
        Snake snake = snakes.get(snakeID);
        Point direction = Directions.values().get(directionID);
        Point nextHeadPoint = Point.add(snake.head(), direction);
        Element element = get(nextHeadPoint.x, nextHeadPoint.y);
        switch (element) {
            case NONE:
                System.out.println(direction + " " + nextHeadPoint);
                snake.move(direction.x, direction.y, false);
                return 1;
            case FOOD:
                snake.move(direction.x, direction.y, true);
                food.remove(nextHeadPoint);
                return 3;
        }
        return 0;
    }

    int moveMe(int directionID) {
        Snake snake = me;
        Point direction = Directions.values().get(directionID);
        Point nextHeadPoint = Point.add(snake.head(), direction);
        Element element = get(nextHeadPoint.x, nextHeadPoint.y);
        switch (element) {
            case NONE:
                snake.move(direction.x, direction.y, false);
                return 1;
            case FOOD:
                snake.move(direction.x, direction.y, true);
                food.remove(nextHeadPoint);
                return 3;
        }
        return 0;
    }
}
