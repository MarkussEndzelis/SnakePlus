package Map;

import java.awt.Point;
import java.util.Set;
import java.util.List;


public interface GameMap {
	String getName();
	Set<Point> getWalls();
	List<MovingObstacle> getMovingObstacles();
}
