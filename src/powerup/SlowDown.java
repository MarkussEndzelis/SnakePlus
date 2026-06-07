package powerup;

import java.awt.Color;

import engine.GameState;

public class SlowDown extends PowerUp{
	
	public SlowDown() {
		super(80);
	}
	@Override
	public void onApply(GameState state) {
		state.setTickInterval(200);
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
		return "SLOW";
	}
	@Override
	public Color getColor() {
		return Color.decode("#74b9ff");
	}
}
