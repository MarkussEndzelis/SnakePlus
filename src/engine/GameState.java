package engine;

import java.awt.Point;
import java.util.Random;

import model.Snake;

public class GameState {
	public static final int COLS = 30;
	public static final int ROWS = 24;
	
	private final Snake snake;
	private Point food;
	private int score = 0;
	private boolean gameOver = false;
	private final Random rng = new Random();
	
	public GameState() {
		snake = new Snake(COLS / 2, ROWS / 2);
		spawnFood();
	}
	
	public void update() {
		if(gameOver) return;
		
		snake.move();
		Point head = snake.getHead();
		
		if(head.x < 0 || head.x >= COLS || head.y < 0 || head.y >= ROWS) {
			gameOver = true;
			return;
		}
		
		if(snake.collidesWithSelf()) {
			gameOver = true;
			return;
		}
		
		if(head.equals(food)) {
			snake.grow();
			score += 10;
			spawnFood();
		}
	}
	
	private void spawnFood() {
		var occupied = snake.getBodySet();
		Point p;
		do {
			p = new Point(rng.nextInt(COLS), rng.nextInt(ROWS));
		}while(occupied.contains(p));
		food = p;
	}
	
	public Snake getSnake() {
		return snake;
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
}
