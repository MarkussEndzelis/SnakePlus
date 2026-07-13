package map;

import java.awt.Point;
import java.util.Collections;
import java.util.Set;
import java.util.List;


public interface GameMap {
	String getName();
	Set<Point> getWalls();
	List<MovingObstacle> getMovingObstacles();
	default Set<Point> getWarningTiles(){
		return Collections.emptySet();
	}
	default void reset() {
		
	}
}
