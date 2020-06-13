package main.gameLogic.stone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * A collection of adjacent Stone(s).
 */
public class Chain {

    public Collection<Stone> stones;
    public Collection<Point> liberties;
    public StoneColor StoneColor;

    public Chain(Stone stone) {
        this(stone.StoneColor);
        addStone(stone);
    }

    public Chain(StoneColor StoneColor) {
        stones = new ArrayList<>();
        liberties = new HashSet<>();
        this.StoneColor = StoneColor;
    }

    public void addStone(Stone stone) {
        stone.chain = this;
        stones.add(stone);
        for (Point point : stone.point.getNeighbors()) {
            if (!stones.contains(stone))
                liberties.add(point);
        }
    }

    public void join(Chain chain) {
        if (chain == this)
            return;
        for (Stone s : chain.stones) {
            addStone(s);
        }
    }

    @Override
    public String toString() {
        String str = "Stones:\n";
        for (Stone stone : stones) {
            str += "" + stone.point.toString() + '\n';
        }
        str += "\nliberties:\n";
        for (Point p : liberties) {
            str += p + "\n";
        }

        return str;
    }
}