package main.gameLogic;

import main.gameLogic.stone.Point;
import main.gameLogic.stone.Stone;
import main.gameLogic.stone.StoneColor;

public class Move {
    public enum Type {
        ADD, REMOVE
    }

    public Type type;
    public Point point;
    public StoneColor color;

    public Move(Type type, Point point, StoneColor color) {
        this.type = type;
        this.point = point;
        this.color = color;
    }

    public Move(Type type, Stone stone) {
        this(type, stone.point, stone.StoneColor);
    }
}