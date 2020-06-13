package main.gameLogic;

import main.gameLogic.board.Board;
import main.gameLogic.stone.Point;
import main.gameLogic.stone.StoneColor;

import java.util.Random;

/**
 * Uses Zobrist hashing to store a hash of the game state.
 * 
 *
 */
public class GridHash {
	private static final int SIZE = Board.SIZE;
	private static long hashValues[][];
	private static final int NUM_OF_POS = SIZE*SIZE, NUM_OF_COLORS = 2; 
	private static final long DEFAULT_SEED = 69_420;
	
	private long hash;
	
	private static void initTable(long seed) {
		Random rand = new Random(seed);
		hashValues = new long[NUM_OF_POS][NUM_OF_COLORS];
		for(int i = 0; i < NUM_OF_POS; i++) {
			for(int j = 0; j < NUM_OF_COLORS; j++) {
				hashValues[i][j] = rand.nextLong();
			}
		}
	}
	
	public GridHash(long hash, long seed) {
		if(hashValues == null) {
			initTable(seed);
		}
		this.hash = hash;
	}
	
	public GridHash(long hash) {
		this(hash, DEFAULT_SEED);
	}
	
	public GridHash(GridHash other) {
		this(other.hash);
	}
	
	public long getHash() {
		return hash;
	}
	
	public void update(Point p, StoneColor color) {
		hash ^= hashValues[p.getFlat()][color.getValue()];
	}
}
