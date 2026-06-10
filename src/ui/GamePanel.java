package ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import engine.Direction;
import engine.GameState;
import map.ClassicMap;
import map.GameMap;
import model.Snake;
import powerup.PowerUp;
import score.CoinManager;
import score.HighScoremanager;
import skin.SkinRegistry;
import skin.SnakeSkin;
import util.FontManager;

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
					startGame(state.getMap());
				}
				if (e.getKeyCode() == KeyEvent.VK_M && state.isGameOver()) {
					if (onBackToMenu != null) {
						onBackToMenu.run();
					}
				}
			}
		});
		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(!state.isGameOver()) {
					return;
				}
				int w = getWidth();
				int h = getHeight();
				int panelW = 340;
				int panelH = 360;
				int px = (w - panelW) / 2;
				int py = (h - panelH) / 2;
				int btnW = 130;
				int btnH = 36;
				int btnY = py + panelH - 50;
				int restartX = px + 20;
				int menuX = px + panelW - 20 - btnW;
				if(e.getX() >= restartX && e.getX() <= restartX + btnW && e.getY() >= btnY && e.getY() <= btnY + btnH) {
					startGame(state.getMap());
					SwingUtilities.invokeLater(() -> requestFocusInWindow());
				}
				if(e.getX() >= menuX && e.getX() <= menuX + btnW && e.getY() >= btnY && e.getY() <= btnY + btnH) {
					if(onBackToMenu != null) {
						onBackToMenu.run();
					}
				}
			}
		});
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
				CoinManager.addCoins(state.getCoinsEarned());
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
		for (int x = 0; x < GameState.COLS; x++) {
			g.drawLine(x * TILE, 0, x * TILE, GameState.ROWS * TILE);
		}
		for (int y = 0; y < GameState.ROWS; y++) {
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
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		g.setFont(FontManager.GAME);
		g.drawString("Score: " + Integer.toString(state.getScore()), 10, 20);

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
			g.setFont(FontManager.GAME_SMALL);
			g.drawString(pu.getName(), 115, barY + 8);
			barY += 14;
		}
		int barH = 30;
		int bottomY = GameState.ROWS * TILE;
		g.setColor(Color.decode("#0d0d1a"));
		g.fillRect(0, bottomY, getWidth(), getHeight() - bottomY);
		g.setColor(new Color(255, 255, 255, 20));
		g.drawLine(0, bottomY, getWidth(), bottomY);
		
		g.setFont(FontManager.GAME);
		fm = g.getFontMetrics();
		g.setColor(Color.decode("#576574"));
		String mapName = state.getMap().getName().toUpperCase();
		g.drawString(mapName, 10, bottomY + 20);
		
		String skinName = currentSkin.getName().toUpperCase();
		g.setColor(currentSkin.getHeadColor());
		g.drawString(skinName, getWidth() - fm.stringWidth(skinName) - 10, bottomY + 20);
		String lengthStr = "LENGTH :" + state.getSnake().getLength();
		g.setColor(Color.WHITE);
		g.drawString(lengthStr, getWidth() / 2 - fm.stringWidth(lengthStr) / 2, bottomY + 20);
		
		String coinsStr = "+" + state.getCoinsEarned() + "c";
		g.setColor(Color.decode("#ffd700"));
		int coinsX = getWidth() / 2 + 80;
		g.drawString(coinsStr, coinsX, bottomY + 20);
		
	}

	private void drawGameOver(Graphics2D g) {
		
		
		int w = getWidth();
		int h = getHeight();
		
		if(w <= 0 || h <= 0) {
			return;
		}
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, w, h);
		
		int panelW = 340;
		int panelH = 360;
		int px = (w - panelW) / 2;
		int py = (h - panelH) / 2;
		g.setColor(Color.decode("#1e2030"));
		g.fillRoundRect(px, py, panelW, panelH, 20, 20);
		g.setColor(Color.decode("#2f3542"));
		g.setStroke(new BasicStroke(2));
		g.drawRoundRect(px, py, panelW, panelH, 20, 20);
		
		FontMetrics fm;
		String text;
		int tx;
		
		if(newHighScore) {
			g.setColor(Color.decode("#ffd32a"));
			g.setFont(FontManager.GAME);
			fm = g.getFontMetrics();
			text = "NEW HIGH SCORE";
			tx = (w - fm.stringWidth(text)) / 2;
			g.drawString(text, tx, py + 38);
		}
		g.setColor(Color.WHITE);
		g.setFont(FontManager.GAME_LARGE);
		fm = g.getFontMetrics();
		text = "GAME OVER";
		tx = (w - fm.stringWidth(text)) / 2;
		g.drawString(text, tx, py + (newHighScore ? 78 : 60));
		
		g.setColor(Color.decode("#2ed573"));
		g.setFont(FontManager.GAME);
		fm = g.getFontMetrics();
		text = "Score: " + Integer.toString(state.getScore());
		tx = (w - fm.stringWidth(text)) / 2;
		g.drawString(text, tx, py + (newHighScore ? 115 : 100));
		
		g.setColor(Color.decode("#2f3542"));
		g.setStroke(new BasicStroke(1));
		g.drawLine(px + 20, py + 130, px + panelW - 20, py + 130);
		
		g.setColor(Color.decode("#a4b0be"));
		g.setFont(FontManager.GAME_SMALL);
		fm = g.getFontMetrics();
		text = "TOP SCORES - " + state.getMap().getName().toUpperCase();
		tx = (w - fm.stringWidth(text)) / 2;
		g.drawString(text, tx, py + 152);
		
		List<Integer> scores = HighScoremanager.getScores(state.getMap().getName());
		for(int i = 0; i < scores.size(); i++) {
			boolean isNew = newHighScore && 1 == 0;
			g.setColor(isNew ? Color.decode("#ffd32a") : Color.WHITE);
			g.setFont(FontManager.GAME);
			fm = g.getFontMetrics();
			text = (i + 1) + ".  " + scores.get(i);
			tx = (w - fm.stringWidth(text)) / 2;
			g.drawString(text, tx, py + 175 + i * 22);
		}
		
		g.setColor(Color.decode("#2f3542"));
		g.drawLine(px + 20, py + panelH - 45, px + panelW - 20, py + panelH - 45);
		
		int btnW = 130;
		int btnH = 36;
		int btnY = py + panelH - 50;
		int restartX = px + 20;
		int menuX = px + panelW - 20 - btnW;
		
		g.setColor(Color.decode("#2ed573"));
		g.fillRoundRect(restartX, btnY, btnW, btnH, 10, 10);
		g.setColor(Color.decode("#0f0f1a"));
		g.setFont(FontManager.GAME_SMALL);
		fm = g.getFontMetrics();
		text = "RESTART";
		g.drawString(text, restartX + (btnW - fm.stringWidth(text)) / 2, btnY + (btnH + fm.getAscent() - fm.getDescent()) / 2);
		
		g.setColor(Color.decode("#576574"));
		g.fillRoundRect(menuX, btnY, btnW, btnH, 10, 10);
		g.setColor(Color.WHITE);
		text = "MAIN MENU";
		g.drawString(text, menuX + (btnW - fm.stringWidth(text)) / 2, btnY + (btnH + fm.getAscent() - fm.getDescent()) / 2);

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
			g.setFont(FontManager.GAME_SMALL);
			String letter = String.valueOf(pu.getName().charAt(0));
			g.drawString(letter, p.x * TILE + 7, p.y * TILE + 16);
		}
	}
}
