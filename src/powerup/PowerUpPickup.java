package powerup;

import java.awt.Point;

public class PowerUpPickup {
	private final PowerUp powerUp;
	private final Point position;
	private int lifetimeTicks;

	public PowerUpPickup(PowerUp powerUp, Point position, int lifetimeTicks) {
		this.powerUp = powerUp;
		this.position = position;
		this.lifetimeTicks = lifetimeTicks;
	}

	public boolean tick() {
		lifetimeTicks--;
		return lifetimeTicks <= 0;
	}

	public int getLifetimeTicks() {
		return lifetimeTicks;
	}

	public PowerUp getPowerUp() {
		return powerUp;
	}

	public Point getPosition() {
		return position;
	}

}
