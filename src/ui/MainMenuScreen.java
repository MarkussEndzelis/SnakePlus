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

public class MainMenuScreen extends JPanel {

	private Runnable onPlay;
	private int animTick = 0;
	private Timer animTimer;
	
	private int snakeOffest = 0;
	
	public MainMenuScreen() {
		setBackground(Color.decode("#0f0f1a"));
		setFocusable(true);
		setPreferredSize(new Dimension(GameState.COLS * GamePanel.TILE, GameState.ROWS * GamePanel.TILE));
		
		animTimer = new Timer(50, e -> {
			animTick++;
			snakeOffest = (snakeOffest + 1) % 30;
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
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int bx = getWidth() / 2 - 100;
				int by = getHeight() / 2 + 20;
				if(e.getX() >= bx && e.getX() <= bx + 200 &&e.getY() >= by && e.getY() <= by + 50) {
					if(onPlay != null) {
						onPlay.run();
					}
				}
			}
		});
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
		drawAnimatedSnake(g2, w, h);
		drawTitle(g2, w, h);
		drawPlayButton(g2, w, h);
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
	private void drawAnimatedSnake(Graphics2D g, int w, int h) {
		int tile = 18;
		int[] snake = new int[16];
		for(int i = 0; i < snake.length; i++) {
			snake[i] = i;
		}
		int baseY = h / 4;
		for(int i = snake.length - 1; i >= 0; i--) {
			int x = (w / 2 - snake.length * tile / 2) + (i + snakeOffest) * tile % (w - tile * 2);
			int y = baseY + (int)(Math.sin((i + animTick * 0.15)) * 18);
			
			if(i == 0) {
				g.setColor(Color.decode("#2ed573"));
			}else {
				float fade = 1f - (i / (float) snake.length) * 0.5f;
				g.setColor(new Color(0x7b, 0xed, 0x9f, (int)(fade * 180)));
			}
			g.fillRoundRect(x, y, tile - 2, tile - 2, 6, 6);
		}
	}
	private void drawTitle(Graphics2D g, int w, int h) {
		Font titleFont = new Font("Arial", Font.BOLD, 72);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		String title = "SNAKE+";
		int tx = (w - fm.stringWidth(title)) / 2;
		int ty = h / 2 - 60;
		
		g.setColor(new Color(0x2e, 0xd5, 0x73, 40));
		g.drawString(title, tx + 2, ty + 2);
		
		float pulse = (float)(Math.sin(animTick * 0.05) * 0.05 + 0.05);
		Color titleColor = blend(Color.decode("#2ed573"), Color.decode("#7bed9f"), pulse);
		g.setColor(titleColor);
		g.drawString(title, tx, ty);
		
	}
	private void drawPlayButton(Graphics2D g, int w, int h) {
		int bw = 200;
		int bh = 50;
		int bx = (w - bw) / 2;
		int by = h / 2 + 20;
		
		g.setColor(new Color(0x2e, 0xd5, 0x73, 40));
		g.fillRoundRect(bx - 4, by - 4, bw + 8, bh + 8, 18, 18);
		
		g.setColor(Color.decode("#2ed573"));
		g.fillRoundRect(bx, by, bw, bh, 14, 14);
		
		g.setColor(Color.decode("#0f0f1a"));
		Font btnFont = new Font("Arial", Font.BOLD, 22);
		g.setFont(btnFont);
		FontMetrics fm = g.getFontMetrics();
		String label = "PLAY";
		int tx = bx + (bw - fm.stringWidth(label)) / 2;
		int ty = by + (bh + fm.getAscent() - fm.getDescent()) / 2;
		g.drawString(label, tx, ty);
	}
	private void drawSubtitle(Graphics2D g, int w, int h) {
		String hint = "Click ENTER / SPACE / PLAY to start";
		Font f = new Font("Arial", Font.PLAIN, 13);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.decode("#576574"));
		g.drawString(hint, (w - fm.stringWidth(hint)) / 2, h / 2 + 100);
		
		String ver = "v1.0 Snake+";
		g.setFont(new Font("Arial", Font.PLAIN, 11));
		fm = g.getFontMetrics();
		g.setColor(new Color(255, 255, 255, 40));
		g.drawString(ver, (w - fm.stringWidth(ver)) / 2, h - 15);
	}
	private Color blend(Color a, Color b, float t) {
		int r = (int)(a.getRed() + (b.getRed() - a.getRed()) * t);
		int gr = (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
		int bl = (int)(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
		return new Color(r, gr, bl);
	}
}
