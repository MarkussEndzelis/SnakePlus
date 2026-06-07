package powerup;

import java.awt.Color;

import engine.GameState;

public class FreezeTime extends PowerUp {
	public FreezeTime() {
		super(60);
	}
	@Override
	public void onApply(GameState state) {
		state.setFrozen(true);
	}
	@Override
	public void onTick(GameState state) {
		
	}
	@Override
	public void onRevert(GameState state) {
		state.setFrozen(false);
	}
	@Override
	public String getName() {
		return "FREEZE";
	}
	@Override
	public Color getColor() {
		return Color.decode("#00cec9");
	}
}
