package gameLogic;

import gameLogic.stone.Point;
import gameLogic.stone.StoneColor;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores a hashed history of the game,
 * and the current hash of the game.
 */
public class GridHistory {
    private GridHash currentHash;
    private GridHash lastHash;
    Set<Long> history;

    public GridHistory() {
        currentHash = new GridHash(0);
        history = new HashSet<>();
        remember();
    }

    /**
     * adds the current hash to history.
     */
    public void remember() {
        history.add(currentHash.getHash());
        lastHash = new GridHash(currentHash);
    }

    /**
     * updates the hash according to some move.
     *
     * @param point
     * @param color
     */
    public void update(Point point, StoneColor color) {
        currentHash.update(point, color);
    }

    public void reroll() {
        currentHash = new GridHash(lastHash);
    }

    /**
     * returns the current hash as a long.
     *
     * @return hash
     */
    public long getHash() {
        return currentHash.getHash();
    }

    /**
     * returns whether the game is in ko - in other words,
     * whether the current state was seen.
     *
     * @return isKo
     */
    public boolean ko() {
        return history.contains(currentHash.getHash());
    }
}

