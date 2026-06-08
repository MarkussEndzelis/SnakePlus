package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import engine.Direction;
import engine.GameState;
import map.ClassicMap;
import map.GameMap;
import model.Snake;
import powerup.PowerUp;
import score.HighScoremanager;
import skin.SkinRegistry;
import skin.SnakeSkin;

public class GamePanel extends JPanel {
	private Runnable onBackToMenu;
	private SnakeSkin currentSkin = SkinRegistry.ALL[0];
	private boolean powerUpsEnabled = true;
	private boolean newHighScore = false;

	public void setPowerUpsEnabled(boolean enabled) {
		powerUpsEnabled = enabled;
	}

	public void setSkin(SnakeSkin skin) {
		this.currentSkin = skin;
	}

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
				Direction d = switch (e.getKeyCode()) {
				case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP;
				case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN;
				case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.LEFT;
				case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
				default -> null;
				};
				if (d != null) {
					state.getSnake().setNextDirection(d);
				}
				if (e.getKeyCode() == KeyEvent.VK_R && state.isGameOver()) {
					startGame(new ClassicMap());
				}
				if (e.getKeyCode() == KeyEvent.VK_M && state.isGameOver()) {
					if (onBackToMenu != null) {
						onBackToMenu.run();
					}
				}
			}
		});
		startGame(new ClassicMap());
	}

	public void startGame(GameMap map) {
		if (timer != null) {
			timer.stop();
		}
		state = new GameState(map, powerUpsEnabled);
		newHighScore = false;
		timer = new Timer(state.getTickInterval(), e -> {
			state.update();
			timer.setDelay(state.getTickInterval());
			repaint();
			if(state.isGameOver()) {
				((Timer) e.getSource()).stop();
				newHighScore = HighScoremanager.isHighScore(state.getMap().getName(), state.getScore());
				HighScoremanager.saveScore(state.getMap().getName(), state.getScore());
			}
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
		drawPowerUps(g2);
		drawSnake(g2);
		drawHUD(g2);

		if (state.isGameOver()) {
			drawGameOver(g2);
		}
	}

	private void drawGrid(Graphics2D g) {
		g.setColor(new Color(255, 255, 255, 12));
		for (int x = 0; x <= GameState.COLS; x++) {
			g.drawLine(x * TILE, 0, x * TILE, GameState.ROWS * TILE);
		}
		for (int y = 0; y <= GameState.ROWS; y++) {
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

		if (state.isGhost()) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}

		for (Point p : body) {
			if (isHead) {
				g.setColor(currentSkin.getHeadColor());
				g.fillRoundRect(p.x * TILE + 1, p.y * TILE + 1, TILE - 2, TILE - 2, 8, 8);
				isHead = false;
			} else {
				g.setColor(currentSkin.getBodyColor());
				g.fillRoundRect(p.x * TILE + 2, p.y * TILE + 2, TILE - 4, TILE - 4, 6, 6);
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

	private void drawHUD(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score: " + state.getScore(), 10, 20);

		if (state.getScoreMultiplier() > 1) {
			g.setColor(Color.decode("#ffd32a"));
			g.drawString("x" + state.getScoreMultiplier(), 120, 20);
		}

		int barY = 35;
		for (PowerUp pu : state.getActiveEffects()) {
			float pct = (float) pu.getTicksRemaining() / pu.getDurationTicks();
			g.setColor(pu.getColor());
			g.fillRoundRect(10, barY, (int) (100 * pct), 8, 4, 4);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 10));
			g.drawString(pu.getName(), 115, barY + 8);
			barY += 14;
		}
	}

	private void drawGameOver(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 160));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int cx = getWidth() / 2;
		int cy = getHeight() / 2;
		
		if(newHighScore) {
			g.setColor(Color.decode("#ffd32a"));
			g.setFont(new Font("Arial", Font.BOLD, 22));
			g.drawString("NEW HIGH SCORE!", cx - 105, cy - 60);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString("GAME OVER", getWidth() / 2 - 95, getHeight() / 2 - 10);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Score: " + state.getScore(), cx - 40, cy + 15);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.setColor(Color.decode("#a4b0be"));
		g.drawString("TOP SCORES - " + state.getMap().getName(), cx - 90, cy + 45);
		
		List<Integer> scores = HighScoremanager.getScores(state.getMap().getName());
		g.setFont(new Font("Arial", Font.PLAIN, 13));
		for(int i = 0; i < scores.size(); i++) {
			boolean isThis = scores.get(i) == state.getScore() && newHighScore && i == 0;
			g.setColor(isThis ? Color.decode("#ffd32a") : Color.WHITE);
			g.drawString((i + 1) + ".  " + scores.get(i), cx - 30, cy + 65 + i * 18);
		}
		
		g.setColor(Color.decode("#576574"));
		g.setFont(new Font("Arial", Font.PLAIN, 13));
		g.drawString("R - restart   M - map select", cx - 100, cy + 165);

	}

	private void drawWalls(Graphics2D g) {
		g.setColor(Color.decode("#576574"));
		for (Point p : state.getMap().getWalls()) {
			g.fillRect(p.x * TILE, p.y * TILE, TILE, TILE);
		}
	}

	private void drawMovingObstacles(Graphics2D g) {
		g.setColor(Color.decode("#ff6b81"));
		for (var mo : state.getMap().getMovingObstacles()) {
			Point p = mo.getPosition();
			g.fillRoundRect(p.x * TILE + 1, p.y * TILE + 1, TILE - 2, TILE - 2, 6, 6);
		}
	}

	private void drawPowerUps(Graphics2D g) {
		for (var pickup : state.getPickups()) {
			Point p = pickup.getPosition();
			PowerUp pu = pickup.getPowerUp();

			g.setColor(pu.getColor());
			g.fillOval(p.x * TILE, p.y * TILE, TILE, TILE);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 11));
			String letter = String.valueOf(pu.getName().charAt(0));
			g.drawString(letter, p.x * TILE + 7, p.y * TILE + 16);
		}
	}
}
