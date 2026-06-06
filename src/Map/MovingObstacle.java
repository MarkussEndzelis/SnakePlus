package Map;

import java.awt.Point;

public class MovingObstacle {
	private int x, y;
	private final boolean horizontal;
	private final int min, max;
	private int dx;
	
	public MovingObstacle(int x, int y, boolean horizontal, int min, int max) {
		this.x = x;
		this.y = y;
		this.horizontal = horizontal;
		this.min = min;
		this.max = max;
		this.dx = 1;
	}
	
	public void update(){
		if(horizontal) {
			x += dx;
			if(x >= max || x <= min) {
				dx *= -1;
			}
		}else {
			y += dx;
			if(y >= max || y <= min) {
				dx *= -1;
			}
		}
	}
	public Point getPosition() {
		return new Point(x, y);
	}
}
