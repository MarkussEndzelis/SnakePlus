package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import engine.GameState;
import util.FontManager;

public class MainMenuScreen extends JPanel {

	private Runnable onPlay;
	private int animTick = 0;
	private Timer animTimer;
	private Runnable onShop;
	private Runnable onSkins;
	private Runnable onLeaderboard;
	
	private int snakeOffest = 0;
	
	public MainMenuScreen() {
		setBackground(Color.decode("#0f0f1a"));
		setFocusable(true);
		setPreferredSize(new Dimension(GameState.COLS * GamePanel.TILE, GameState.ROWS * GamePanel.TILE + 140));
		
		animTimer = new Timer(50, e -> {
			animTick++;
			repaint();
		});
		animTimer.start();
		
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
					if(onPlay != null) {
						onPlay.run();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_S) {
					if(onShop != null) {
						onShop.run();
					}
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int bx = getWidth() / 2 - 100;
				int by = getHeight() / 2 - 30;
				if(e.getX() >= bx && e.getX() <= bx + 200 &&e.getY() >= by && e.getY() <= by + 50) {
					if(onPlay != null) {
						onPlay.run();
					}
				}
				int sy = by + 54;
				if(e.getX() >= bx && e.getX() <= bx + 200 && e.getY() >= sy && e.getY() <= sy + 50) {
					if(onShop != null) {
						onShop.run();
					}
				}
				int skinsY = sy + 50 + 16;
				if(e.getX() >= bx && e.getX() <= bx + 200 && e.getY() >= skinsY && e.getY() <= skinsY + 50) {
					if(onSkins != null) {
						onSkins.run();
					}
				}
				int leaderboardY = skinsY + 50 + 16;
				if(e.getX() >= bx && e.getX() <= bx + 200 && e.getY() >= leaderboardY && e.getY() <= leaderboardY + 50) {
					if(onLeaderboard != null) {
						onLeaderboard.run();
					}
				}
			}
		});
	}
	public void setOnSkins(Runnable onSkins) {
		this.onSkins = onSkins;
	}
	public void setOnLeaderboard(Runnable onLeaderboard) {
		this.onLeaderboard = onLeaderboard;
	}
	public void setOnPlay(Runnable onPlay) {
		this.onPlay = onPlay;
	}
	public void startAnim() {
		animTimer.start();
	}
	public void stopAnim() {
		animTimer.stop();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		drawBackground(g2, w, h);
		drawLogoSnake(g2, w, h);
		//drawAnimatedSnake(g2, w, h);
		drawTitle(g2, w, h);
		drawButtons(g2, w, h);
		drawSubtitle(g2, w, h);
	}
	private void drawBackground(Graphics2D g, int w, int h) {
		g.setColor(new Color(255, 255, 255, 6));
		int tile = 24;
		for(int x = 0; x < w; x+= tile) {
			g.drawLine(x, 0, x, h);
		}
		for(int y = 0; y < h; y+= tile) {
			g.drawLine(0, y, w, y);
		}
	}
//	private void drawAnimatedSnake(Graphics2D g, int w, int h) {
//		int tile = 18;
//		int len = 16;
//		int baseY = h / 4;
//		int totalWidth = w + len * tile;
//		
//		for(int i = len - 1; i >= 0; i--) {
//			int rawX = w - ((snakeOffest * 3 + i * tile) % totalWidth);
//			int y = baseY + (int)(Math.sin((i * 0.5) + animTick * 0.15) * 18);
//			
//			if(rawX < -tile || rawX > w + tile) {
//				continue;
//			}
//			if(i == 0) {
//				g.setColor(Color.decode("#2ed573"));
//			}else {
//				float fade = 1f - (i / (float) len) * 0.5f;
//				g.setColor(new Color(0x7b, 0xed, 0x9f, (int)(fade * 180)));
//			}
//			g.fillRoundRect(rawX, y, tile - 2, tile - 2, 6, 6);
//		}
//		
//		}
	
	private void drawLogoSnake(Graphics2D g, int w, int h) {
		int tile = 20;
		int startX = (w / 2) - (6 * tile);
		int startY = h / 4 - tile * 3;
		
		int[][] shape = {
				{0,0},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{9,0},{10,0},{11,0},
				{11,1},{11,2},
				{10,2},{9,2},{8,2},{7,2},{6,2},{5,2},{4,2},{3,2},{2,2},{1,2},{0,2},
				{0,3},{0,4},
				{1,4},{2,4},{3,4},{4,4},{5,4},{6,4},{7,4},{8,4},{9,4},{10,4},{11,4}
		};
		for(int i = 0; i < shape.length; i++) {
			int px = startX + shape[i][0] * tile;
			int py = startY + shape[i][1] * tile;
			
			if(i == 0) {
				g.setColor(Color.decode("#2ed573"));
				g.fillRoundRect(px, py, tile - 2, tile - 2, 7, 7);
				
				g.setColor(Color.decode("#0f0f1a"));
				g.fillOval(px + 4, py + 4, 4, 4);
				g.fillOval(px + 11, py + 4, 4, 4);
			}else {
				float fade = 1f - (i / (float) shape.length) * 0.4f;
				int green = (int)(0xed * fade);
				g.setColor(new Color(0x7b, green, 0x9f, (int)(fade * 220)));
				g.fillRoundRect(px + 1, py + 1, tile - 4 , tile - 4, 5, 5);
			}
		}
	}
	private void drawTitle(Graphics2D g, int w, int h) {
		Font titleFont = FontManager.GAME_LARGE;
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		String title = "SNAKE+";
		int tx = (w - fm.stringWidth(title)) / 2;
		int ty = h / 2 - 70;
		
		g.setColor(new Color(0x2e, 0xd5, 0x73, 40));
		g.drawString(title, tx , ty + 2);
		
		float pulse = (float)(Math.sin(animTick * 0.05) * 0.05 + 0.05);
		Color titleColor = blend(Color.decode("#2ed573"), Color.decode("#7bed9f"), pulse);
		g.setColor(titleColor);
		g.drawString(title, tx, ty);
		
	}
	private void drawButtons(Graphics2D g, int w, int h) {
		int bw = 200;
		int bh = 44;
		int bx = (w - bw) / 2;
		int by = h / 2 - 30;
		
		g.setColor(Color.decode("#2ed573"));
		g.fillRoundRect(bx, by, bw, bh, 14, 14);
		
		g.setColor(Color.decode("#0f0f1a"));
		g.setFont(FontManager.GAME);
		
		FontMetrics fm = g.getFontMetrics();
		String label = "PLAY";
		g.drawString(label, bx + (bw - fm.stringWidth(label)) / 2, by + (bh + fm.getAscent() - fm.getDescent()) / 2);
		
		int sy = by + bh + 10;
		g.setColor(Color.decode("#ffd700"));
		g.fillRoundRect(bx, sy, bw, bh, 14, 14);
		g.setColor(Color.decode("#0f0f1a"));
		label = "SHOP";
		g.drawString(label, bx + (bw - fm.stringWidth(label)) / 2, sy + (bh + fm.getAscent() - fm.getDescent()) / 2);
		
		int skinsY = sy + bh + 10;
		g.setColor(Color.decode("#00cfff"));
		g.fillRoundRect(bx, skinsY, bw, bh, 14, 14);
		g.setColor(Color.decode("#0f0f1a"));
		label = "SKINS";
		g.drawString(label, bx + (bw - fm.stringWidth(label)) / 2, skinsY + (bh + fm.getAscent() - fm.getDescent()) / 2);
		
		int leaderboardY = skinsY + bh + 10;
		g.setColor(Color.decode("#ff6b81"));
		g.fillRoundRect(bx, leaderboardY, bw, bh, 14, 14);
		g.setColor(Color.decode("#0f0f1a"));
		label = "LEADERBOARD";
		g.drawString(label, bx + (bw - fm.stringWidth(label)) / 2, leaderboardY + (bh + fm.getAscent() - fm.getDescent()) / 2);
	}
	private void drawSubtitle(Graphics2D g, int w, int h) {
		String ver = "v1.0 Snake+";
		g.setFont(FontManager.GAME_SMALL);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(new Color(255, 255, 255, 40));
		g.drawString(ver, (w - fm.stringWidth(ver)) / 2, h - 15);
	}
	private Color blend(Color a, Color b, float t) {
		int r = (int)(a.getRed() + (b.getRed() - a.getRed()) * t);
		int gr = (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
		int bl = (int)(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
		return new Color(r, gr, bl);
	}
	@Override
	public void addNotify() {
		super.addNotify();
		requestFocusInWindow();
	}
	public void setOnShop(Runnable onShop) {
		this.onShop = onShop;
	}
}
