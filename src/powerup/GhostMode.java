package powerup;

import java.awt.Color;

import engine.GameState;

public class GhostMode extends PowerUp {
	public GhostMode() {
		super(70);
	}
	@Override
	public void onApply(GameState state) {
		state.setGhost(true);
	}
	@Override
	public void onTick(GameState state) {
		
	}
	@Override
	public void onRevert(GameState state) {
		state.setGhost(false);
	}
	@Override
	public String getName() {
		return "GHOST";
	}
	@Override
	public Color getColor() {
		return Color.decode("#dfe6e9");
	}
}
