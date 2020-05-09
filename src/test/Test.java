package test;

import main.Grid;
import main.Point;
import main.StoneColor;

import static main.StoneColor.getColor;

public class Test {
	static final int[][] TEST_GRID0 = {
			{0, 0, 2, 0, 1, 0, 0, 1, 0},
			{0, 2, 0, 0, 1, 1, 1, 1, 1},
			{2, 1, 1, 1, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{1, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 1, 0, 0, 0, 0, 0, 0}
	};

	static final int[][] TEST_GRID1 = {
			{1, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 0, 0, 0, 0, 0, 1},
			{1, 1, 0, 0, 0, 0, 0, 0, 1},
			{1, 0, 0, 0, 2, 0, 0, 0, 1},
			{1, 0, 0, 2, 0, 2, 0, 0, 1},
			{1, 0, 0, 0, 2, 0, 0, 0, 1},
			{1, 0, 0, 0, 0, 0, 0, 0, 1},
			{1, 0, 0, 1, 0, 0, 0, 1, 1},
			{1, 1, 1, 0, 1, 1, 1, 1, 0}
	};

	static final int[][] TEST_GRID2 = {
			{1, 0, 2, 0, 1, 1, 2, 0, 0},
			{0, 2, 2, 1, 1, 2, 2, 0, 0},
			{2, 2, 1, 1, 0, 2, 0, 0, 0},
			{1, 1, 0, 1, 0, 2, 0, 0, 0},
			{0, 0, 0, 1, 0, 2, 0, 0, 0},
			{0, 0, 0, 1, 0, 2, 0, 2, 2},
			{0, 0, 0, 1, 0, 2, 2, 1, 1},
			{0, 0, 1, 1, 2, 2, 1, 1, 0},
			{0, 0, 1, 2, 2, 0, 1, 0, 1}
	};



	private static Grid getGrid(int[][] arr) {
		Grid grid = new Grid(arr.length);
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[0].length; j++) {
				if(arr[i][j] == 0) continue;
				grid.addStone(i, j, getColor(arr[i][j]));
			}
		}

		return grid;
	}

	public static void test() {
		Grid grid = getGrid(TEST_GRID0);
		Grid.RegionMap map = grid.getRegions();
		for(Grid.Region r: map.regions) {
			System.out.println(r.occup.name());
			for(Point p: r.points)
				System.out.println(p);
			System.out.println("is small? " + (grid.isSmall(r, StoneColor.BLACK) || grid.isSmall(r, StoneColor.WHITE)));

			for(Grid.Chain c: grid.getVitalChains(r)) {
				System.out.println("chain:");
				System.out.println(c);
			}
		}

		System.out.println("ACTUAL SCORING DEBUG");

		int black = grid.getScore(StoneColor.BLACK), white = grid.getScore(StoneColor.WHITE);
		System.out.println("BLACK:");
		System.out.println(black);
		System.out.println("WHITE:");
		System.out.println(white);
		
		System.out.println("Influence info:");
		float[][] influence = grid.getInfluence();
		for(int i = 0; i < grid.SIZE; i++) {
			for(int j = 0; j < grid.SIZE; j++) {
				System.out.printf("%.2f\t", influence[i][j]);
			}
			System.out.println();
		}
	}
}
