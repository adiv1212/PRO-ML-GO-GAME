package main;

import java.util.ArrayList;
import java.util.Collection;

/**
 * represents a 2D point on a grid.
 * 
 *
 */
public class Point {
	final int row, col;
	private Collection<Point> neighbors;
	private static final int SIZE = Board.SIZE;

	Point(int row, int col) {
		this.row = row;
		this.col = col;
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