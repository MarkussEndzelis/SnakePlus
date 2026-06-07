package engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import map.GameMap;
import map.MovingObstacle;
import model.Snake;
import powerup.FreezeTime;
import powerup.GhostMode;
import powerup.Magnet;
import powerup.PowerUp;
import powerup.PowerUpPickup;
import powerup.ScoreMultiplier;
import powerup.ShrinkSnake;
import powerup.SlowDown;
import powerup.SpeedBoost;

public class GameState {
	public static final int COLS = 30;
	public static final int ROWS = 24;

	private final Snake snake;
	private final GameMap map;
	private Point food;
	private int score = 0;
	private boolean gameOver = false;
	private final Random rng = new Random();
	private int scoreMultiplier = 1;
	private int tickInterval = 120;
	private boolean frozen = false;
	private boolean ghost = false;

	private final List<PowerUpPickup> pickups = new ArrayList<>();
	private final List<PowerUp> activeEffects = new ArrayList<>();
	private int foodEatenSinceLastPowerUp = 0;

	private static final PowerUp[] POOL = { new SpeedBoost(), new SlowDown(), new ScoreMultiplier(), new ShrinkSnake(),
			new FreezeTime(), new GhostMode(), new Magnet() };

	public GameState(GameMap map) {
		this.map = map;
		snake = new Snake(COLS / 2, ROWS / 2);
		spawnFood();
	}

	public void update() {
		if (gameOver)
			return;
		
		activeEffects.removeIf(e -> e.tick(this));
		pickups.removeIf(PowerUpPickup::tick);

		if(!frozen) {
			for (MovingObstacle mo : map.getMovingObstacles()) {
				mo.update();
			}
		}

		snake.move();
		Point head = snake.getHead();
		if(!ghost) {
			if (head.x < 0 || head.x >= COLS || head.y < 0 || head.y >= ROWS) {
				gameOver = true;
				return;
			}

			if (map.getWalls().contains(head)) {
				gameOver = true;
				return;
			}

			for (MovingObstacle mo : map.getMovingObstacles()) {
				if (mo.getPosition().equals(head)) {
					gameOver = true;
					return;
				}
			}

			if (snake.collidesWithSelf()) {
				gameOver = true;
				return;
			}
		}else {
			Point wrapped = new Point((head.x + COLS) % COLS, (head.y + ROWS) % ROWS);
			if(!wrapped.equals(head)) {
				snake.teleportHead(wrapped);
			}
		}
		
		if(head.equals(food)) {
			snake.grow();
			score += 10 * scoreMultiplier;
			foodEatenSinceLastPowerUp++;
			if(foodEatenSinceLastPowerUp >= 3) {
				spawnPowerUp();
				foodEatenSinceLastPowerUp = 0;
			}
			spawnFood();
		}
		
		pickups.removeIf(pickup -> {
			if(pickup.getPosition().equals(head)) {
				PowerUp pu = pickup.getPowerUp();
				pu.apply(this);
				activeEffects.add(pu);
				return true;
			}
			return false;
		});

		
	}

	private void spawnFood() {
		var occupied = snake.getBodySet();
		occupied.addAll(map.getWalls());

		for (var mo : map.getMovingObstacles()) {
			occupied.add(mo.getPosition());
		}
		Point p;
		do {
			p = new Point(rng.nextInt(COLS), rng.nextInt(ROWS));
		} while (occupied.contains(p));
		food = p;
	}
	private void spawnPowerUp() {
		if(pickups.size() >= 2) {
			return;
		}
		var occupied = getOccupied();
		Point p;
		int tries = 0;
		do {
			p = new Point(rng.nextInt(COLS), rng.nextInt(ROWS));
			tries++;
		}while(occupied.contains(p) && tries < 100);
		
		PowerUp pu = POOL[rng.nextInt(POOL.length)];
		pu = freshPowerUp(rng.nextInt(POOL.length));
		pickups.add(new PowerUpPickup(pu, p, 150));
	}
	private PowerUp freshPowerUp(int index) {
		return switch(index) {
		case 0 -> new SpeedBoost();
		case 1 -> new SlowDown();
		case 2 -> new ScoreMultiplier();
		case 3 -> new ShrinkSnake();
		case 4 -> new FreezeTime();
		case 5 -> new GhostMode();
		default -> new Magnet();
		};
	}
	private Set<Point> getOccupied(){
		Set<Point> occupied = snake.getBodySet();
		occupied.addAll(map.getWalls());
		for(var mo : map.getMovingObstacles()) {
			occupied.add(mo.getPosition());
		}
		for(var pu : pickups) {
			occupied.add(pu.getPosition());
		}
		if(food != null) {
			occupied.add(food);
			
		}
		return occupied;
	}
	

	public Snake getSnake() {
		return snake;
	}

	public GameMap getMap() {
		return map;
	}

	public Point getFood() {
		return food;
	}

	public int getScore() {
		return score;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setTickInterval(int ms) {
		tickInterval = ms;
	}

	public int getTickInterval() {
		return tickInterval;
	}

	public void setScoreMultiplier(int m) {
		scoreMultiplier = m;
	}

	public int getScoreMultiplier() {
		return scoreMultiplier;
	}

	public void setFrozen(boolean f) {
		frozen = f;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setGhost(boolean g) {
		ghost = g;
	}

	public boolean isGhost() {
		return ghost;
	}

	public void setFood(Point p) {
		food = p;
	}
	public List<PowerUpPickup> getPickups(){
		return pickups;
	}
	public List<PowerUp> getActiveEffects(){
		return activeEffects;
	}

}
