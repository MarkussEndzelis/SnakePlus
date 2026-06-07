package powerup;

import engine.GameState;

public abstract class PowerUp {

	protected int durationTicks;
	private int ticksRemaining;
	private boolean active = false;

	public PowerUp(int durationTicks) {
		this.durationTicks = durationTicks;
	}

	public void apply(GameState state) {
		active = true;
		ticksRemaining = durationTicks;
		onApply(state);
	}

	public boolean tick(GameState state) {
		if (!active) {
			return false;
		}
		ticksRemaining--;
		onTick(state);
		if (ticksRemaining <= 0) {
			onRevert(state);
			active = false;
			return true;
		}
		return false;
	}
	public int getTicksRemaining() {
		return ticksRemaining;
	}
	public int getDurationTicks() {
		return durationTicks;
	}
	public boolean isActive() {
		return active;
	}
	public abstract void onApply(GameState state);
	public abstract void onTick(GameState state);
	public abstract void onRevert(GameState state);
	public abstract String getName();
	public abstract java.awt.Color getColor();
}
