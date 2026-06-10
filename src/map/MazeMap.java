package map;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MazeMap implements GameMap {

	private final Set<Point> walls = new HashSet<>();
	
	public MazeMap() {
		for(int x = 0; x < 30; x++) {
			walls.add(new Point(x, 0));
			walls.add(new Point(x, 23));
		}
		for(int y = 0; y < 24; y++) {
			walls.add(new Point(0, y));
			walls.add(new Point(29, y));
		}
		for(int x = 2; x <= 12; x++) {
			walls.add(new Point(x, 4));
		}
		for(int x = 17; x <= 27; x++) {
			walls.add(new Point(x, 4));
		}
		for(int x = 2; x <= 8; x++) {
			walls.add(new Point(x, 8));
		}
		for(int x = 13; x <= 20; x++) {
			walls.add(new Point(x, 8));
		}
		for(int x = 4; x <= 14; x++) {
			walls.add(new Point(x, 12));
		}
		for(int x = 19; x <= 27; x++) {
			walls.add(new Point(x, 12));
		}
		for(int x = 2; x <= 10; x++) {
			walls.add(new Point(x, 16));
		}
		for(int x = 15; x <= 22; x++) {
			walls.add(new Point(x, 16));
		}
		for(int x = 5; x <= 13; x++) {
			walls.add(new Point(x, 20));
		}
		for(int x = 18; x <= 27; x++) {
			walls.add(new Point(x, 20));
		}
		
		
		
		for(int y = 4; y <= 8; y++) {
			walls.add(new Point(14, y));
		}
		for(int y = 4; y <= 8; y++) {
			walls.add(new Point(22, y));
		}
		for(int y = 8; y <= 12; y++) {
			walls.add(new Point(6, y));
		}
		for(int y = 4; y <= 8; y++) {
			walls.add(new Point(24, y));
		}
		for(int y = 12; y <= 16; y++) {
			walls.add(new Point(10, y));
		}
		for(int y = 12; y <= 16; y++) {
			walls.add(new Point(18, y));
		}
		for(int y = 16; y <= 20; y++) {
			walls.add(new Point(3, y));
		}
		for(int y = 16; y <= 20; y++) {
			walls.add(new Point(26, y));
		}
		
		
		
		
	}
	@Override
	public String getName() {
		return "Maze";
	}
	@Override
	public Set<Point> getWalls(){
		return walls;
	}
	@Override
	public List<MovingObstacle> getMovingObstacles(){
		return List.of();
	}
}
