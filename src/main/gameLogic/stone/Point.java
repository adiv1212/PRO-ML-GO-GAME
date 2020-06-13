package main.gameLogic.stone;

import main.gameLogic.Board;

import java.util.ArrayList;
import java.util.Collection;

/**
 * represents a 2D point on a grid.
 */
public class Point {
    private int row;
    private int col;
    private Collection<Point> neighbors;
    private static final int SIZE = Board.SIZE;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // get an iterable object for all neighboring Points.
    public Iterable<Point> getNeighbors() {
        if (neighbors == null) {
            neighbors = new ArrayList<>();
            if (row > 0) {
                neighbors.add(new Point(row - 1, col));
            }
            if (row < SIZE - 1) {
                neighbors.add(new Point(row + 1, col));
            }
            if (col > 0) {
                neighbors.add(new Point(row, col - 1));
            }
            if (col < SIZE - 1) {
                neighbors.add(new Point(row, col + 1));
            }
        }
        return neighbors;
    }

    public int getFlat() {
        return row * SIZE + col;
    }

    public double getSqrDist(Point other) {
        return Math.pow(row - other.row, 2) + Math.pow(col - other.col, 2);
    }

    public double getDist(Point other) {
        return Math.sqrt(getSqrDist(other));
    }

    // equals and hashCode are needed for it to be used as a key
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Point))
            return false;

        Point other = (Point) obj;
        return this.row == other.row && this.col == other.col;
    }

    // for hashtable
    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    // for debugging
    @Override
    public String toString() {
        return row + " " + col;
    }
}