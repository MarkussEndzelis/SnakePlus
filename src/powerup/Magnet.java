package powerup;

import java.awt.Color;
import java.awt.Point;

import engine.GameState;

public class Magnet extends PowerUp {
	public Magnet() {
		super(90);
	}
	
	@Override
	public void onApply(GameState state){
		
	}
	
	@Override
	public void onTick(GameState state) {
		Point head = state.getSnake().getHead();
		Point food = state.getFood();
		int dx = Integer.signum(head.x - food.x);
		int dy = Integer.signum(head.y - food.y);
		Point newFood = new Point(food.x + dx, food.y + dy);
		
		if(!state.getMap().getWalls().contains(newFood) && !state.getSnake().getBodySet().contains(newFood)) {
			state.setFood(newFood);
		}
	}
	@Override
	public void onRevert(GameState state) {
		
	}
	@Override
	public String getName() {
		return "MAGNET";
	}
	@Override
	public Color getColor() {
		return Color.decode("#fd79a8");
	}
}
