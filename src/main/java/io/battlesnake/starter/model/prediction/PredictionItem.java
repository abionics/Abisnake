package io.battlesnake.starter.model.prediction;

import io.battlesnake.starter.help.Point;
import io.battlesnake.starter.model.Core;
import io.battlesnake.starter.model.Directions;
import io.battlesnake.starter.model.Element;
import io.battlesnake.starter.model.Snake;

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
