package main.java.gameLogic.stone;

import java.util.*;

import main.java.gameLogic.Grid;

/**
 * A collection of adjacent Stone(s).
 */
public class Chain {

    public Collection<Stone> stones;
    public Collection<Point> liberties;
    public StoneColor StoneColor;

    public Chain(Stone stone) {
        this(stone.StoneColor);
        this.addStone(stone);
    }

    public Chain(StoneColor StoneColor) {
        this.stones = new ArrayList<>();
        this.liberties = new HashSet<>();
        this.StoneColor = StoneColor;
    }

    public void addStone(Stone stone) {
        stone.chain = this;
        this.stones.add(stone);
        for (Point p : stone.point.getNeighbors()) {
            if (!Grid.stones.containsKey(p))
                liberties.add(p);
        }
    }

    public void join(Chain chain) {
        if (chain == this)
            return;
        for (Stone s : chain.stones) {
            this.addStone(s);
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