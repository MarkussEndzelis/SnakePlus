package powerup;

import java.awt.Color;

import engine.GameState;

public class ShrinkSnake extends PowerUp {
	public ShrinkSnake() {
		super(0);
	}
	
	@Override
	public void onApply(GameState state) {
		state.getSnake().shrink(4);
	}
	@Override
	public void onTick(GameState state) {
		
	}
	@Override
	public void onRevert(GameState state) {
		
	}
	@Override
	public String getName() {
		return "SHRINK";
	}
	@Override
	public Color getColor() {
		return Color.decode("#a29bfe");
	}
}
