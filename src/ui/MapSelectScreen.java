package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import map.ClassicMap;
import map.GameMap;
import map.ObstacleMap;

public class MapSelectScreen extends JPanel{
	private final GameMap[] maps = {
			new ClassicMap(),
			new ObstacleMap()
	};
	private int selected = 0;
	private Runnable onSelect;
	
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
			}
		});
	}
	public void setOnSelect(Runnable onSelect) {
		this.onSelect = onSelect;
	}
	public GameMap getSelectedMap() {
		return maps[selected];
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 36));
		g2.drawString("SELECT MAP", w / 2 - 105, 80);
		
		int cardW = 200;
		int cardH = 260;
		int spacing = 40;
		int totalW = maps.length * cardW + (maps.length - 1) * spacing;
		int startX = (w - totalW) / 2;
		int cardY = h / 2 - cardH / 2;
		
		for(int i = 0; i < maps.length; i++) {
			int cardX = startX + i * (cardW + spacing);
			boolean isSelected = i == selected;
			
			g2.setColor(isSelected ? Color.decode("#2f3542") : Color.decode("#1e2030"));
			g2.fillRoundRect(cardX, cardY, cardW, cardH, 16, 16);
			
			g2.setColor(isSelected ? Color.decode("#2ed573") : Color.decode("#576574"));
			g2.setStroke(new BasicStroke(isSelected ? 3 : 1));
			g2.drawRoundRect(cardX, cardY, cardW, cardH, 16, 16);
			
			drawMapPreview(g2, maps[i], cardX + 20, cardY + 20, cardW - 40, 140);
			
			g2.setColor(isSelected ? Color.decode("#2ed573") : Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 18));
			g2.drawString(maps[i].getName(), cardX + 20, cardY + 185);
			
			g2.setColor(Color.decode("#a4b0be"));
			g2.setFont(new Font("Arial", Font.PLAIN, 12));
			String desc = getDescription(maps[i]);
			g2.drawString(desc, cardX + 20, cardY + 210);
			
			if(isSelected) {
				g2.setColor(Color.decode("#2ed573"));
				g2.setFont(new Font("Arial", Font.BOLD, 13));
				g2.drawString("PRESS ENTER", cardX + 42, cardY + 245);
			}
		}
		g2.setColor(Color.decode("#576574"));
		g2.setFont(new Font("Arial", Font.PLAIN, 13));
		g2.drawString("<- -> to browse   ENTER to play", w / 2 - 115, h - 30);
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
		case "Classic" -> "Open grid, no obstacles";
		case "Obstacle Course" -> "Walls + moving obstacles";
		default -> "";
		};
	}
}
