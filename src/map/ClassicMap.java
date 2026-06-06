package map;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ClassicMap implements GameMap {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Classic";
	}

	@Override
	public Set<Point> getWalls() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public List<MovingObstacle> getMovingObstacles() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

}
