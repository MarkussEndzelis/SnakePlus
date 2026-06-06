package Map;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObstacleMap implements GameMap {

	private final Set<Point> walls = new HashSet<>();
	private final List<MovingObstacle> movers;
	
	public ObstacleMap() {
		for(int i = 10; i <= 20; i++) {
			walls.add(new Point(i, 12));
		}
		for(int i = 8; i <= 16; i++) {
			walls.add(new Point(15, i));
		}
		
		for(int x = 3; x <= 6; x++) {
			for(int y = 3; y <= 5; y++) {
				walls.add(new Point(x, y));
			}
		}
		for(int x = 23; x <= 26; x++) {
			for(int y = 18; y <= 21; y++) {
				walls.add(new Point(x, y));
			}
		}
		movers = List.of(new MovingObstacle(5, 20, true, 2, 10), 
						 new MovingObstacle(24, 5, false, 2, 10), 
						 new MovingObstacle(15, 20, true, 10, 25));
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Obstacle Course";
	}

	@Override
	public Set<Point> getWalls() {
		// TODO Auto-generated method stub
		return walls;
	}

	@Override
	public List<MovingObstacle> getMovingObstacles() {
		// TODO Auto-generated method stub
		return movers;
	}
	
}
