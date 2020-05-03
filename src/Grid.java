import java.util.*;

/**
 * Provides game logic.
 *  
 *
 */
public class Grid {


private void DEBUG() {
	int[][] liberties = new int[SIZE][SIZE];
	int[][] chain_liberties = new int[SIZE][SIZE];
	Collection<Chain> chains = new ArrayList<>();
	for(int i = 0; i < SIZE; i++) {
		for(int j = 0; j < SIZE; j++) {
			liberties[i][j] = -1;
			chain_liberties[i][j] = -1;
		}
	}
	for(Point p : stones.keySet()) {
		Stone s = stones.get(p);
		liberties[p.row][p.col] = s.liberties;
		chain_liberties[p.row][p.col] = s.chain.getLiberties();
		if(!chains.contains(s.chain)) {
			chains.add(s.chain);
		}
	}
	System.out.println("\nLIBERTIES:");
	for(int i = 0; i < SIZE; i++) {
		for(int j = 0; j < SIZE; j++) {
			int x = liberties[i][j];
			char c = (x == -1) ? '.' : (char)('0' + x);
			System.out.print(c + " ");
		}
		System.out.println();
	}
	
	System.out.println("\nCHAIN LIBERTIES:");
	for(int i = 0; i < SIZE; i++) {
		for(int j = 0; j < SIZE; j++) {
			int x = chain_liberties[i][j];
			char c = (x == -1) ? '.' : (char)('0' + x);
			System.out.print(c + " ");
		}
		System.out.println();
	}
	
	System.out.println("CHAINS:");
	for(Chain c: chains) {
		System.out.println(c);
	}
}
	
private int pass;
private GridHistory history;
public Map<Point, Stone> stones;
private Collection<Move> queuedMoves; 
private final int SIZE;
private static final float KOMI = 6.5f;

private static class Move {
	public enum Type {
		ADD, REMOVE
	}
	
	public Type type;
	public Point pos;
	public StoneColor color;
	
	public Move(Type type, Point pos, StoneColor color) {
		this.type = type;
		this.pos = pos;
		this.color = color;
	}
	
	public Move(Type type, int row, int col, StoneColor color) {
		this(type, new Point(row, col), color);
	}
	
	public Move(Type type, Stone stone) {
		this(type, stone.row, stone.col, stone.StoneColor);
	}
}



public Stone getStone(Move m) {
	return new Stone(m.pos, m.color);
}

/**
 * Basic game element.
 *
 */
public class Stone {

	public Chain chain;
	public StoneColor StoneColor;
	public int liberties; // to be set by Grid
	// Row and col are need to remove (set to null) this Stone from Grid
	public int row;
	public int col;
	
	public Stone(int row, int col, StoneColor StoneColor) {
	    chain = null;
	    this.StoneColor = StoneColor;
	    this.row = row;
	    this.col = col;
	    liberties = 0;
	}
	
	public Stone(Point p, StoneColor color ) { 
		this(p.row, p.col, color);
	}
	
	@Override
	public String toString() {
		return StoneColor.toString();
	}
}

/**
 * A collection of adjacent Stone(s).
 *
 */
public class Chain {

	public Collection<Stone> stones;
	private Collection<Point> liberties;
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
	
	public int getLiberties() {
		return liberties.size();
	}
	
	public void addStone(Stone stone) {
		stone.chain = this;
		stones.add(stone);
		Point newP = new Point(stone.row, stone.col);
		for(Point p: newP.getNeighbors()) {
			if(!isOccupied(p))
				liberties.add(p);
		}
	}
	
	public void join(Chain chain) {
		if(chain == this)
			return;
		for(Stone s: chain.stones) {
			addStone(s);
		}
	}

	@Override
	public String toString() {
		String str = "Stones:\n";
		for(Stone s: stones) {
			str += "" + s.row + ' ' + s.col + '\n'; 
		}
		str += "\nliberties:\n";
		for(Point p: liberties) {
			str += p + "\n";
		}
		
		return str;
	}
}

public Grid(int size) {
	history = new GridHistory();
    stones = new HashMap<>();
    queuedMoves = new ArrayList<>();
    pass = 0;
    SIZE = size;
}

public void passTurn() {
	if(over()) return;
	pass++;
}

private void conductMove(Move m) {
	switch(m.type) {
	
	case ADD:
		Stone newStone = getStone(m);
		stones.put(m.pos, newStone);
		Chain chain = new Chain(newStone);
		
		for(Point neighbor: m.pos.getNeighbors()) {
			if(!isOccupied(neighbor)) {
				newStone.liberties++;
				continue;
			}
			Stone neighborStone = stones.get(neighbor);
			neighborStone.liberties--;
			neighborStone.chain.liberties.remove(m.pos);
			
			if(neighborStone.StoneColor == m.color) {
				chain.join(neighborStone.chain);
			}
		}
		break;
		
	case REMOVE:
		stones.remove(m.pos);
		for(Point neighbor: m.pos.getNeighbors()) {
			if(!isOccupied(neighbor)) continue;
			Stone s = stones.get(neighbor);
			s.liberties++;
			s.chain.liberties.add(m.pos);
		}
		
	}
}

private void conductQueue() {
	for(Move m: queuedMoves)
		conductMove(m);
	queuedMoves.clear();
}

private boolean isSuicide(Point p, StoneColor StoneColor) {
    Stone tempStone;
    StoneColor tempStoneColor;
    int chainLiberties;
    for(Point neighbor : p.getNeighbors()) {
        // If there's no stone, it's not a suicide move.
        if(!isOccupied(neighbor))
        	return false;
        
        tempStone = stones.get(neighbor);
        tempStoneColor = tempStone.StoneColor;
        chainLiberties = tempStone.chain.getLiberties();
        
        if(tempStoneColor == StoneColor && chainLiberties > 1
        		|| tempStoneColor != StoneColor && chainLiberties <= 1)
        	return false;
    }
    return true;
}

/**
 * Adds Stone to Grid.
 * 
 * @param row
 * @param col
 * @param color
 */


public boolean addStone(int row, int col, StoneColor color) {
	return addStone(new Point(row, col), color);
}

public boolean addStone(Point point, StoneColor color) {
//	System.out.println("move at: " + point);
//	System.out.println("nieghbors: ");
//	for(Point p : point.getNeighbors()) {
//		System.out.println(p);
//	}
	if(over()) return false;
		
    if(isOccupied(point)) {
		System.out.println("Someone is sitting in your spot.");
		return false;
    }
	if(isSuicide(point, color)) {
    	System.out.println("Suicide move.");
    	return false;
    }
	
	
	history.remember();
	history.update(point, color);
    queuedMoves.add(new Move(Move.Type.ADD, point, color));
	
	for(Point neighbor: point.getNeighbors()) {
		if(!isOccupied(neighbor))
			continue;
		
		Stone neighborStone = stones.get(neighbor);
		StoneColor neighborColor = neighborStone.StoneColor;
        if (neighborColor != color) {
        	Chain chain = neighborStone.chain;
        	if(chain.getLiberties() == 1) {
            	for(Stone stone: chain.stones) {
            		history.update(stone.row, stone.col, stone.StoneColor);
            	    queuedMoves.add(new Move(Move.Type.REMOVE, stone));
            	}
            }
        }
	}
	
    if(history.ko()) {
    	System.out.println("Ko.");
    	history.reroll();
    	queuedMoves.clear();
    	return false;
    }
    
    conductQueue();
    //System.out.println(stones.get(point).liberties);
    pass = 0;
	DEBUG();
    return true;
}

/*******************************************************************/
/*******************************************************************/
/*******************************************************************/

public enum Occup {
	BLACK, WHITE, UNDECIDED
}

public static class Region {
	Occup occup;
	Collection<Point> points;
	
	Region() {
		points = new ArrayList<>();
	}
	
	Region(Occup occup, Collection<Point> points) {
		this.occup = occup;
		this.points = points;
	}
	
	@Override
	public String toString() {
		String result = occup.name() + '\n';
		for(Point p: points) {
			result += p;
			result += '\n';
		}
		result += '\n';
		return result;
	}
}

public static class RegionMap {
	Collection<Region> regions;
	Map<Point, Region> regionMap;
	
	RegionMap(Collection<Region> regions, 
			Map<Point, Region> regionMap) {
		this.regions = regions;
		this.regionMap = regionMap;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(Region r: regions)
			result += r;
		return result;
	}
}

private Occup getOccup(StoneColor color) {
	switch(color) {
	case BLACK:
		return Occup.BLACK;
	case WHITE:
		return Occup.WHITE;
	default:
		return Occup.UNDECIDED;
	}
}

/////////////////////////////////////////////////////////////////////////////////////////

// assuming region previously had Occupation prev, and a stone whose color is color, finds the next occup.

private Occup newOccup(Occup prev, StoneColor color) {
	if(prev == null)
		return getOccup(color);
	
	switch(prev) {
	case UNDECIDED:
		return Occup.UNDECIDED;
	case BLACK:
		return color == StoneColor.WHITE ? Occup.UNDECIDED : Occup.BLACK;
	case WHITE:
		return color == StoneColor.BLACK ? Occup.UNDECIDED : Occup.WHITE;
	default:
		return getOccup(color);
	}
}

// subtask of getRegions
private Region getRegion(int i, int j, Map<Point, Region> regionMap) {
	Queue<Point> s = new ArrayDeque<>();
	Region region = new Region();
	s.add(new Point(i, j));
	
	while(!s.isEmpty()) {
		Point p = s.remove();
		
		if(!regionMap.containsKey(p)) {
			if(stones.containsKey(p)) {
				region.occup = newOccup(region.occup, stones.get(p).StoneColor);
				continue;
			}
			
			region.points.add(p);
			regionMap.put(p, region);
			for(Point n: p.getNeighbors()) {
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
	
	for(int i = 0; i < SIZE; i++) {
		for(int j = 0; j < SIZE; j++) {
			Point p = new Point(i, j);
			if(!regionMap.containsKey(p) && !stones.containsKey(p))
				regions.add(getRegion(i, j, regionMap));
		}
	}
	
	return new RegionMap(regions, regionMap);
}

// returns a list of all the chains on the board
private Set<Chain> getChains() {
	Set<Chain> chains = new HashSet<>();
	
	for(Point p: stones.keySet()) {
		chains.add(stones.get(p).chain);
	}
	
	return chains;
}

// return whether point p has no liberties occupied
private boolean isFree(Point p) {
	for(Point n: p.getNeighbors()) {
		if(stones.containsKey(n))
			return false;
	}
	return true;
}

// returns whether a region is small and of some color, AKA is surrounded by that color and has no free points 
public boolean isSmall(Region r, StoneColor color) {
	if(r.occup != getOccup(color)) return false;
	
	for(Point p: r.points) {
		if(isFree(p))
			return false;
	}
	
	return true;
}

public Set<Chain> getVitalChains(Region r) {
	Set<Chain> chains = getChains();
	
	for(Point p: r.points) {
		Set<Chain> newChains = new HashSet<>();
		for(Point n: p.getNeighbors()) {
			if(stones.containsKey(n)) {
				Chain c = stones.get(n).chain;
				if(chains.contains(c))
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
	
	for(Region r: regions.regions)
		if(isSmall(r, color))
			smallRegions.add(r);
	
	Map<Region, Set<Chain>> regionMap = new HashMap<>();
	Map<Chain, Set<Region>> chainMap = new HashMap<>();
	for(Region r: smallRegions) {
		Set<Chain> vitalChains = getVitalChains(r);
		regionMap.put(r, vitalChains);
		
		for(Chain c: vitalChains) {
			if(!chainMap.containsKey(c))
				chainMap.put(c, new HashSet<>());
			chainMap.get(c).add(r);
		}
	}
	
	boolean run = true;
	while(run) {
		run = false;
		for(Chain c: chainMap.keySet()) {
			Set<Region> chainRegions = chainMap.get(c);  
			if(chainRegions.size() == 1) {
				run = true;
				for(Region r: chainRegions) {
					for(Chain ch: regionMap.get(r)) {
						chainMap.get(ch).remove(r);
					}
					regionMap.get(r).clear();
				}
				chainRegions.clear();
			}
		}
	}

	int score = 0;
	for(Chain c: chainMap.keySet()) {
		if(chainMap.get(c).size() > 0) {
//			System.out.println("chain:");
//			System.out.println(c);
			score += c.stones.size();
		}
	}
	
	for(Region r: regionMap.keySet()) {
		if(regionMap.get(r).size() > 0) {
//			System.out.println("region:");
//			System.out.println(r);
			score += r.points.size();
		}
	}
	
	return score;
}

public StoneColor winner() {
	//removeDeadStones();
	int black = getScore(StoneColor.BLACK);
	int white = getScore(StoneColor.WHITE);
	
	if(black > white + KOMI) {
		return StoneColor.BLACK;
	} else {
		return StoneColor.WHITE;
	}
}

/**
 * Returns true if given position is occupied by any stone
 * 
 * @param point
 * @return true if given position is occupied
 */

private boolean isOccupied(Point point) {
    return stones.containsKey(point);
}

/**
 * Returns StoneColor (black/white) of given position or null if it's unoccupied.
 * Needs valid row and column.
 * 
 * @param row
 * @param col
 * @return
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
	// TODO Auto-generated method stub
	return pass == 2;
}
}
