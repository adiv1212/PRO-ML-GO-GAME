package main.gameLogic.stone;

/**
 * Basic game element.
 */
public class Stone {

    public Chain chain;
    public StoneColor StoneColor;
    public int liberties; // to be set by Grid
    public Point point;

    public Stone(Point point, StoneColor StoneColor) {
        chain = null;
        this.StoneColor = StoneColor;
        this.point = point;
        liberties = 0;
    }

    @Override
    public String toString() {
        return StoneColor.toString();
    }
}