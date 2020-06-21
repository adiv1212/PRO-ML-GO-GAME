package main.java.gameLogic;

import java.util.*;

import main.java.gameLogic.stone.Chain;
import main.java.gameLogic.stone.Point;
import main.java.gameLogic.stone.Stone;
import main.java.gameLogic.stone.StoneColor;


/**
 * Provides game logic.
 */
public class Grid {

    public static final float KOMI = 6.5f;
    public static final float REACH = 1.5f; // Influence function parameter
    public final int SIZE;
    public static Map<Point, Stone> stones;
    private int pass;
    private final GridHistory history;
    private final Collection<Move> queuedMoves;

    public Grid(int size) {
        history = new GridHistory();
        stones = new HashMap<>();
        queuedMoves = new ArrayList<>();
        pass = 0;
        SIZE = size;
    }

    public Stone getStone(Move m) {
        return new Stone(m.point, m.color);
    }

    public void passTurn() {
        if (over()) return;
        pass++;
    }

    private void conductMove(Move m) {
        switch (m.type) {

            case ADD:
                Stone newStone = getStone(m);
                stones.put(m.point, newStone);
                Chain chain = new Chain(newStone);

                for (Point neighbor : m.point.getNeighbors()) {
                    if (!stones.containsKey(neighbor)) {
                        newStone.liberties++;
                        continue;
                    }
                    Stone neighborStone = stones.get(neighbor);
                    neighborStone.liberties--;
                    neighborStone.chain.liberties.remove(m.point);

                    if (neighborStone.StoneColor == m.color) {
                        chain.join(neighborStone.chain);
                    }
                }
                break;

            case REMOVE:
                stones.remove(m.point);
                for (Point neighbor : m.point.getNeighbors()) {
                    if (!stones.containsKey(neighbor))
                        continue;
                    Stone stone = stones.get(neighbor);
                    stone.liberties++;
                    stone.chain.liberties.add(m.point);
                }

        }
    }

    private void conductQueue() {
        for (Move m : queuedMoves)
            conductMove(m);
        queuedMoves.clear();
    }

    private boolean isSuicide(Point p, StoneColor StoneColor) {
        Stone tempStone;
        StoneColor tempStoneColor;
        int chainLiberties;
        for (Point neighbor : p.getNeighbors()) {
            // If there's no stone, it's not a suicide move.
            if (!stones.containsKey(neighbor))
                return false;

            tempStone = stones.get(neighbor);
            tempStoneColor = tempStone.StoneColor;
            chainLiberties = tempStone.chain.liberties.size();

            if (tempStoneColor == StoneColor && chainLiberties > 1
                    || tempStoneColor != StoneColor && chainLiberties <= 1)
                return false;
        }
        return true;
    }

    public boolean addStone(Point point, StoneColor color) {
        if (over()) return false;
        if (stones.containsKey(point)) {
            System.out.println("Someone is sitting in your spot.");
            return false;
        }
        if (isSuicide(point, color)) {
            System.out.println("Suicide move.");
            return false;
        }
        history.remember();
        history.update(point, color);
        queuedMoves.add(new Move(Move.Type.ADD, point, color));

        for (Point neighbor : point.getNeighbors()) {
            if (!stones.containsKey(neighbor))
                continue;

            Stone neighborStone = stones.get(neighbor);
            StoneColor neighborColor = neighborStone.StoneColor;
            if (neighborColor != color) {
                Chain chain = neighborStone.chain;
                if (chain.liberties.size() == 1) {
                    for (Stone stone : chain.stones) {
                        history.update(stone.point, stone.StoneColor);
                        queuedMoves.add(new Move(Move.Type.REMOVE, stone));
                    }
                }
            }
        }

        if (history.ko()) {
            System.out.println("Ko.");
            history.reroll();
            queuedMoves.clear();
            return false;
        }

        conductQueue();
        //System.out.println(stones.get(point).liberties);
        pass = 0;
        //System.out.println("ADDED STONE");
        return true;
    }

    // assuming region previously had Occupation prev, and a stone whose color is color, finds the next occup.
    private Occup newOccup(Occup prev, StoneColor color) {
        if (prev == null)
            return Occup.getOccup(color);

        switch (prev) {
            case UNDECIDED:
                return Occup.UNDECIDED;
            case BLACK:
                return color == StoneColor.WHITE ? Occup.UNDECIDED : Occup.BLACK;
            case WHITE:
                return color == StoneColor.BLACK ? Occup.UNDECIDED : Occup.WHITE;
            default:
                return Occup.getOccup(color);
        }
    }

    // subtask of getRegions
    private Region getRegion(int i, int j, Map<Point, Region> regionMap) {
        Queue<Point> s = new ArrayDeque<>();
        Region region = new Region();
        s.add(new Point(i, j));

        while (!s.isEmpty()) {
            Point p = s.remove();

            if (!regionMap.containsKey(p)) {
                if (stones.containsKey(p)) {
                    region.occup = newOccup(region.occup, stones.get(p).StoneColor);
                    continue;
                }

                region.points.add(p);
                regionMap.put(p, region);
                for (Point n : p.getNeighbors()) {
                    s.add(n);
                }
            }
        }

        return region;
    }

    // returns a map of all regions
    public RegionMap getRegions() {
        Collection<Region> regions = new ArrayList<>();
        Map<Point, Region> regionMap = new HashMap<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Point p = new Point(i, j);
                if (!regionMap.containsKey(p) && !stones.containsKey(p))
                    regions.add(getRegion(i, j, regionMap));
            }
        }

        return new RegionMap(regions, regionMap);
    }

    // returns a list of all the chains on the board
    private Set<Chain> getChains() {
        Set<Chain> chains = new HashSet<>();

        for (Point p : stones.keySet()) {
            chains.add(stones.get(p).chain);
        }

        return chains;
    }

    // return whether point p has no liberties occupied
    private boolean isFree(Point p) {
        for (Point n : p.getNeighbors()) {
            if (stones.containsKey(n))
                return false;
        }
        return true;
    }

    // returns whether a region is small and of some color, AKA is surrounded by that color and has no free points
    public boolean isSmall(Region r, StoneColor color) {
        if (r.occup != Occup.getOccup(color)) return false;

        for (Point p : r.points) {
            if (isFree(p))
                return false;
        }

        return true;
    }

    public Set<Chain> getVitalChains(Region r) {
        Set<Chain> chains = getChains();

        for (Point p : r.points) {
            Set<Chain> newChains = new HashSet<>();
            for (Point n : p.getNeighbors()) {
                if (stones.containsKey(n)) {
                    Chain c = stones.get(n).chain;
                    if (chains.contains(c))
                        newChains.add(c);
                }
            }
            chains = newChains;
        }

        return chains;
    }

    // returns the score of some color using the following algorithm:
// - initialize a list of all small regions of that color
// - initialize a list of all chains of that color
// - initialize a relation between regions and chains saying if a chain is vital to a region.
// - repeat:
// - - remove all chains with less than two vital regions
// - - for each chain, remove the regions it's related to.
    public int getScore(StoneColor color) {
        RegionMap regions = getRegions();
        List<Region> smallRegions = new ArrayList<>();

        for (Region r : regions.regions)
            if (isSmall(r, color))
                smallRegions.add(r);

        Map<Region, Set<Chain>> regionMap = new HashMap<>();
        Map<Chain, Set<Region>> chainMap = new HashMap<>();
        for (Region r : smallRegions) {
            Set<Chain> vitalChains = getVitalChains(r);
            regionMap.put(r, vitalChains);

            for (Chain c : vitalChains) {
                if (!chainMap.containsKey(c))
                    chainMap.put(c, new HashSet<>());
                chainMap.get(c).add(r);
            }
        }

        boolean run = true;
        while (run) {
            run = false;
            for (Chain c : chainMap.keySet()) {
                Set<Region> chainRegions = chainMap.get(c);
                if (chainRegions.size() == 1) {
                    run = true;
                    for (Region r : chainRegions) {
                        for (Chain ch : regionMap.get(r)) {
                            chainMap.get(ch).remove(r);
                        }
                        regionMap.get(r).clear();
                    }
                    chainRegions.clear();
                }
            }
        }

        int score = 0;
        for (Chain c : chainMap.keySet()) {
            if (chainMap.get(c).size() > 0) {
                score += c.stones.size();
            }
        }

        for (Region r : regionMap.keySet()) {
            if (regionMap.get(r).size() > 0) {
                score += r.points.size();
            }
        }

        return score;
    }

    public StoneColor winner() {
        int black = getScore(StoneColor.BLACK);
        int white = getScore(StoneColor.WHITE);

        if (black > white + KOMI) {
            return StoneColor.BLACK;
        } else {
            return StoneColor.WHITE;
        }
    }

    public float[][] getInfluence() {
        float[][] influence = new float[SIZE][SIZE];
        for (Point point : stones.keySet()) {
            int sign = stones.get(point).StoneColor.getSign();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    double sqrDist = point.getSqrDist(new Point(i, j));
                    influence[i][j] += sign * Math.exp(-sqrDist / Math.pow(REACH, 2) / 2);
                }
            }
        }
        return influence;
    }

    /**
     * Returns StoneColor (black/white) of given position or null if it's unoccupied.
     * Needs valid row and column.
     */
    public StoneColor getStoneColor(int row, int col) {
        Stone stone = stones.get(new Point(row, col));
        if (stone == null) {
            return null;
        } else {
            return stone.StoneColor;
        }
    }

    public boolean over() {
        return pass == 2;
    }
}
