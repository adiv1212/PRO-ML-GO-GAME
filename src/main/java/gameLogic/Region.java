package main.java.gameLogic;

import java.util.*;

import main.java.gameLogic.stone.Point;

public class Region {
    public Occup occup;
    public Collection<Point> points;

    Region() {
        points = new ArrayList<>();
    }

    @Override
    public String toString() {
        String result = occup.name() + '\n';
        for (Point p : points) {
            result += p;
            result += '\n';
        }
        result += '\n';
        return result;
    }
}