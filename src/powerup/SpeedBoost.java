package powerup;

import engine.GameState;
import java.awt.Color;



public class SpeedBoost extends PowerUp {
	public SpeedBoost() {
		super(80);
	}
	@Override
	public void onApply(GameState state) {
		state.setTickInterval(60);
	}
	@Override
	public void onTick(GameState state) {
		
	}
	@Override
	public void onRevert(GameState state) {
		state.setTickInterval(120);
	}
	@Override
	public String getName() {
		return "SPEED";
	}
	@Override
	public Color getColor() {
		return Color.decode("#ffa502");
	}
}
