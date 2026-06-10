package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import map.ClassicMap;
import map.GameMap;
import map.MazeMap;
import map.ObstacleMap;
import util.FontManager;

public class MapSelectScreen extends JPanel{
	private final GameMap[] maps = {
			new ClassicMap(),
			new ObstacleMap(),
			new MazeMap()
	};
	
	private int selected = 0;
	private Runnable onSelect;
	private boolean powerUpsEnabled = true;
	
	public MapSelectScreen() {
		setBackground(Color.decode("#1a1a2e"));
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					selected = (selected - 1 + maps.length) % maps.length;
					repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					selected = (selected + 1) % maps.length;
					repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER && onSelect != null) {
					onSelect.run();
				}
				if(e.getKeyCode() == KeyEvent.VK_P) {
					powerUpsEnabled = !powerUpsEnabled;
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int w = getWidth();
				int h = getHeight();
				int cardW = 200;
				int cardH = 280;
				int spacing = 20;
				int totalW = maps.length * cardW + (maps.length - 1) * spacing;
				int startX = (w - totalW) / 2;
				int cardY = h / 2 - cardH / 2;
				
				for(int i = 0; i < maps.length; i++) {
					int cardX = startX + i * (cardW + spacing);
					if(e.getX() >= cardX && e.getX() <= cardX + cardW && e.getY() >= cardY && e.getY() <= cardY + cardH) {
						 selected = i;
						 if(onSelect != null) {
							 onSelect.run();
							 return;
						 }
					}
					
				}
				int bx = w / 2 - 80;
				int by = h - 70;
				if(e.getX() >= bx && e.getX() <= bx + 240 && e.getY() >= by - 5 && e.getY() <= by + 25) {
					powerUpsEnabled = !powerUpsEnabled;
					repaint();
				}
			}
		});
	}
	public void setOnSelect(Runnable onSelect) {
		this.onSelect = onSelect;
	}
	public GameMap getSelectedMap() {
		return maps[selected];
	}
	public boolean isPowerUpsEnabled() {
		return powerUpsEnabled;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		g2.setColor(Color.WHITE);
		g2.setFont(FontManager.GAME_LARGE);
		FontMetrics fm = g2.getFontMetrics();
		String title = "SELECT MAP";
		g2.drawString(title, (w - fm.stringWidth(title)) / 2, 80);
		
		int cardW = 200;
		int cardH = 280;
		int spacing = 20;
		int totalW = maps.length * cardW + (maps.length - 1) * spacing;
		int startX = (w - totalW) / 2;
		int cardY = h / 2 - cardH / 2;
		
		for(int i = 0; i < maps.length; i++) {
			int cardX = startX + i * (cardW + spacing);
			boolean isSelected = i == selected;
			
			g2.setColor(isSelected ? Color.decode("#2f3542") : Color.decode("#1e2030"));
			g2.fillRoundRect(cardX, cardY, cardW, cardH, 16, 16);
			
			g.setColor(Color.decode("#576574"));
			g2.setStroke(new BasicStroke(1));
			g2.drawRoundRect(cardX, cardY, cardW, cardH, 16, 16);
			
			drawMapPreview(g2, maps[i], cardX + 20, cardY + 20, cardW - 40, 140);
			
			g2.setColor(isSelected ? Color.decode("#2ed573") : Color.WHITE);
			g2.setFont(FontManager.GAME);
			g2.drawString(maps[i].getName(), cardX + 20, cardY + 185);
			
			g2.setColor(Color.decode("#a4b0be"));
			g2.setFont(FontManager.GAME_SMALL);
			String desc = getDescription(maps[i]);
			g2.drawString(desc, cardX + 20, cardY + 210);
			
			
		}
		g2.setColor(Color.decode("#576574"));
		g2.setFont(FontManager.GAME_SMALL);
		int bx = w / 2 - 80;
		int by = h - 70;
		g2.setColor(powerUpsEnabled ? Color.decode("#2ed573") : Color.decode("#576574"));
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(bx, by, 20, 20, 4, 4);
		if(powerUpsEnabled) {
			g2.setColor(Color.decode("#2ed573"));
			g2.fillRoundRect(bx, by, 20, 20, 4, 4);
		}
		g2.setColor(Color.WHITE);
		g2.setFont(FontManager.GAME_SMALL);
		g2.drawString("Power-ups (toggle)", bx + 28, by + 15);
	}
	
	private void drawMapPreview(Graphics2D g, GameMap map, int x, int y, int w, int h) {
		g.setColor(Color.decode("#0f0f1a"));
		g.fillRoundRect(x, y, w, h, 8, 8);
		
		int previewCols = 30;
		int previewRows = 24;
		float tileW = (float) w / previewCols;
		float tileH = (float) h / previewRows;
		
		g.setColor(Color.decode("#576574"));
		for(java.awt.Point p : map.getWalls()) {
			g.fillRect((int)(x + p.x * tileW), (int)(y + p.y * tileH), Math.max(2, (int)tileW), Math.max(2, (int)tileH));
		}
		g.setColor(Color.decode("#ff6b81"));
		for(var mo : map.getMovingObstacles()) {
			java.awt.Point p = mo.getPosition();
			g.fillOval((int)(x + p.x * tileW), (int)(y + p.y * tileH), Math.max(3, (int)tileW), Math.max(3, (int)tileH));
		}
	}
	private String getDescription(GameMap map) {
		return switch(map.getName()) {
		case "Classic" -> "Open grid";
		case "Obstacle map" -> "Walls + obstacles";
		case "Maze" -> "Tight corridors";
		default -> "";
		};
	}
}
