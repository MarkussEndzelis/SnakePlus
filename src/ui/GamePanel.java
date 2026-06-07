package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import engine.Direction;
import engine.GameState;
import map.ClassicMap;
import map.GameMap;
import model.Snake;

public class GamePanel extends JPanel {
	private Runnable onBackToMenu;
	public void setOnBackToMenu(Runnable r) {
		onBackToMenu = r;
	}
	
	public static final int TILE = 24;
	
	private GameState state;
	private Timer timer;
	
	public GamePanel() {
		setPreferredSize(new Dimension(GameState.COLS * TILE, GameState.ROWS * TILE));
		setBackground(Color.decode("#1a1a2e"));
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Direction d = switch(e.getKeyCode()) {
				case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP;
				case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN;
				case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.LEFT;
				case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
				default -> null;
				};
				if(d != null) {
					state.getSnake().setNextDirection(d);
				}
				if(e.getKeyCode() == KeyEvent.VK_R && state.isGameOver()) {
					startGame(new ClassicMap());
				}
				if(e.getKeyCode() == KeyEvent.VK_M && state.isGameOver()) {
					if(onBackToMenu != null) {
						onBackToMenu.run();
					}
				}
			}
		});
		startGame(new ClassicMap());
	}
	public void startGame(GameMap map) {
		if(timer != null) {
			timer.stop();
		}
		state = new GameState(map);
		timer = new Timer(120, e -> {
			state.update();
			repaint();
			if(state.isGameOver()) ((Timer) e.getSource()).stop();
		});
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawGrid(g2);
		drawWalls(g2);
		drawMovingObstacles(g2);
		drawFood(g2);
		drawSnake(g2);
		drawHUD(g2);
		
		if(state.isGameOver()) {
			drawGameOver(g2);
		}
	}
	private void drawGrid(Graphics2D g) {
		g.setColor(new Color(255, 255, 255, 12));
		for(int x = 0; x <= GameState.COLS; x++) {
			g.drawLine(x * TILE, 0, x * TILE, GameState.ROWS * TILE);
		}
		for(int y = 0; y <= GameState.ROWS; y++) {
			g.drawLine(0, y * TILE, GameState.COLS * TILE, y * TILE);
		}
	}

	private void drawFood(Graphics2D g) {
		Point food = state.getFood();
		g.setColor(Color.decode("#ff4757"));
		g.fillOval(food.x * TILE + 3, food.y * TILE + 3, TILE - 6, TILE - 6);
	}
	private void drawSnake(Graphics2D g) {
		Snake snake = state.getSnake();
		var body = snake.getBody();
		boolean isHead = true;
		for(Point p : body) {
			if(isHead) {
				g.setColor(Color.decode("#2ed573"));
				g.fillRoundRect(p.x * TILE + 1, p.y * TILE + 1, TILE - 2, TILE - 2, 8, 8);
				isHead = false;
			}else {
				g.setColor(Color.decode("#7bed9f"));
				g.fillRoundRect(p.x * TILE + 2, p.y * TILE + 2, TILE - 4, TILE - 4, 6, 6);
			}
		}
	}
	private void drawHUD(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score: " + state.getScore(), 10, 20);
	}
	private void drawGameOver(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString("GAME OVER", getWidth() / 2 - 95, getHeight() /2 - 10);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Press R to restart", getWidth() / 2 - 70, getHeight() /2 + 25);
		g.drawString("Score: " + state.getScore(), getWidth() / 2 - 40, getHeight() / 2 + 50);
		g.drawString("Press M for map select", getWidth() / 2 - 85, getHeight() / 2 + 75);
		
	}
	private void drawWalls(Graphics2D g) {
		g.setColor(Color.decode("#576574"));
		for(Point p : state.getMap().getWalls()) {
			g.fillRect(p.x * TILE, p.y * TILE, TILE, TILE);
		}
	}
	private void drawMovingObstacles(Graphics2D g) {
		g.setColor(Color.decode("#ff6b81"));
		for(var mo : state.getMap().getMovingObstacles()) {
			Point p = mo.getPosition();
			g.fillRoundRect(p.x * TILE + 1, p.y * TILE + 1, TILE - 2, TILE -2, 6, 6);
		}
	}
}
