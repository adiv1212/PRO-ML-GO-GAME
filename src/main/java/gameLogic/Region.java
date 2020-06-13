package gameLogic;

import gameLogic.stone.Point;

import java.util.ArrayList;
import java.util.Collection;

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