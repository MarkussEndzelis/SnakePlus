package powerup;

import java.awt.Color;

import engine.GameState;

public class ScoreMultiplier extends PowerUp {
	public ScoreMultiplier() {
		super(100);
	}
	@Override
	public void onApply(GameState state) {
		state.setScoreMultiplier(2);
	}
	@Override
	public void onTick(GameState state) {
		
	}
	@Override
	public void onRevert(GameState state) {
		state.setScoreMultiplier(120);
	}
	@Override
	public String getName() {
		return "x2 SCORE";
	}
	@Override
	public Color getColor() {
		return Color.decode("#ffd32a");
	}
}
