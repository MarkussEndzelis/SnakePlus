package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import skin.SkinRegistry;
import skin.SnakeSkin;
import util.FontManager;

public class SkinSelectScreen extends JPanel {

	private int selected = 0;
	private Runnable onSelect;
	
	public SkinSelectScreen() {
		setBackground(Color.decode("#1a1a2e"));
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					selected = (selected - 1 + SkinRegistry.ALL.length) % SkinRegistry.ALL.length;
					repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					selected = (selected + 1) % SkinRegistry.ALL.length;
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
	public SnakeSkin getSelectedSkin() {
		return SkinRegistry.ALL[selected];
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
		String title = "SELECT SKIN";
		g2.drawString(title, (w - fm.stringWidth(title)) / 2, 80);
		
		int cardW = 140;
		int cardH = 160;
		int spacing = 20;
		int cols = 4;
		int rows = (int) Math.ceil( SkinRegistry.ALL.length / (double) cols);
		int totalW = cols * cardW + (cols - 1) * spacing;
		int startX = (w - totalW) / 2;
		int startY = h / 2 - (rows * (cardH + spacing)) / 2;
		
		for(int i = 0; i < SkinRegistry.ALL.length; i++) {
			SnakeSkin skin = SkinRegistry.ALL[i];
			int col = i % cols;
			int row = i / cols;
			int cardX = startX + col * (cardW + spacing);
			int cardY = startY + row * (cardH + spacing);
			boolean isSelected = i == selected;
			
			g2.setColor(isSelected ? Color.decode("#2f3542") : Color.decode("#1e2030"));
			g2.fillRoundRect(cardX, cardY, cardW, cardH, 14, 14);
			
			g2.setColor(isSelected ? skin.getHeadColor() : Color.decode("#576574"));
			g2.setStroke(new BasicStroke(isSelected ? 3 : 1));
			g2.drawRoundRect(cardX, cardY, cardW, cardH, 14, 14);
			
			drawSnakePreview(g2, skin, cardX + 10, cardY + 15, cardW - 20, 90);
			
			g2.setColor(isSelected ? skin.getHeadColor() : Color.WHITE);
			g2.setFont(FontManager.GAME);
			g2.drawString(skin.getName(), cardX + 12, cardY + 125);
			
			if(isSelected) {
				g2.setColor(skin.getHeadColor());
				g2.setFont(FontManager.GAME_SMALL);
				g2.drawString("PRESS ENTER", cardX + 12, cardY + 148);
			}
		}
		g2.setColor(Color.decode("#576574"));
		g2.setFont(FontManager.GAME_SMALL);
		g2.drawString("<- -> to browse   ENTER to confirm", w / 2 - 130, h - 30);
	}
	private void drawSnakePreview(Graphics2D g, SnakeSkin skin, int x, int y, int w, int h) {
		
		g.setColor(Color.decode("#0f0f1a"));
		g.fillRoundRect(x, y, w, h, 8, 8);
		
		int tile = 14;
		int cols = w / tile;
		int rows = h / tile;
		
		int[][] shape = {
				{1,1},{2,1},{3,1},{4,1},{5,1},{6,1},{6,2},{6,3},{5,3},{4,3},{3,3},{2,3},{1,3},{1,4},{1,5}	
		};
		
		for(int i = 0; i < shape.length; i++) {
			int px = x + shape[i][0] * tile;
			int py = y + shape[i][1] * tile;
			if(i == 0) {
				g.setColor(skin.getHeadColor());
				g.fillRoundRect(px, py, tile - 1, tile - 1, 5, 5);
			}else {
				g.setColor(skin.getBodyColor());
				g.fillRoundRect(px + 1, py + 1, tile - 3, tile - 3, 4, 4);
			}
		}
	}
}
