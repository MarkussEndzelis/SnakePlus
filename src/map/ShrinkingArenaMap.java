package map;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.GameState;

public class ShrinkingArenaMap implements GameMap {
	
	private static final long SHRINK_INTERVAL_MS = 8000;
	private static final int MAX_SHRINK_LEVEL = 8;
	
	private long startTime;
	
	public ShrinkingArenaMap() {
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public String getName() {
		return "Shrinking Arena";
	}
	
	private int getShrinkLevel() {
		long elapsed = System.currentTimeMillis() - startTime;
		int level = (int) (elapsed / SHRINK_INTERVAL_MS);
		return Math.min(level, MAX_SHRINK_LEVEL);
	}
	
	@Override
	public Set<Point> getWalls(){
		Set<Point> walls = new HashSet<>();
		int level = getShrinkLevel();
		for (int d = 0; d < level; d++) {
			addRing(walls, d);
		}
		return walls;
	}
	
	@Override
	public Set<Point> getWarningTiles(){
		Set<Point> warning = new HashSet<>();
		int level = getShrinkLevel();
		if (level < MAX_SHRINK_LEVEL) {
			addRing(warning, level);
		}
		return warning;
	}
	
	private void addRing(Set<Point> set, int depth) {
		int minX = depth;
		int maxX = GameState.COLS - 1 - depth;
		int minY = depth;
		int maxY = GameState.ROWS - 1 - depth;
		if (minX > maxX || minY > maxY) return;
		
		for(int x = minX; x <= maxX; x++) {
			set.add(new Point(x, minY));
			set.add(new Point(x, maxY));
		}
		for (int y = minY; y <= maxY; y++) {
			set.add(new Point(minX, y));
			set.add(new Point(maxX, y));
		}
	}
	
	@Override
	public List<MovingObstacle> getMovingObstacles(){
		return Collections.emptyList();
	}
	
	@Override
	public void reset() {
		this.startTime = System.currentTimeMillis();
	}
}
