package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import engine.GameState;
import score.SkinManager;
import score.SkinRegistry;
import score.UnlockManager;
import skin.SnakeSkin;
import util.FontManager;

public class SkinsScreen extends JPanel {

	private Runnable onBack;
	private int selected = 0;
	
	public SkinsScreen() {
		setBackground(Color.decode("#0f0f1a"));
		setFocusable(true);
		setPreferredSize(new Dimension(GameState.COLS * GamePanel.TILE, GameState.ROWS * GamePanel.TILE + 160));
		
		
		String activeName = SkinManager.getActiveSkin().getName();
		for(int i = 0; i < SkinRegistry.ALL.length; i++) {
			if(SkinRegistry.ALL[i].getName().equals(activeName)) {
				selected = i;
				break;
			}
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int w = getWidth();
				int cardW = 118;
				int cardH = 135;
				int spacing = 12;
				int cols = 3;
				int totalW = cols * cardW + ( cols - 1) * spacing;
				int startX = (w - totalW) / 2;
				int startY = 80;
				
				for(int i = 0; i < SkinRegistry.ALL.length; i++) {
					if(!UnlockManager.isUnlocked(SkinRegistry.ALL[i].getName())) {
						continue;
					}
					int col = i % cols;
					int row = i / cols;
					int cx = startX + col * (cardW + spacing);
					int cy = startY + row * (cardH + spacing);
					if(e.getX() >= cx && e.getX() <= cx + cardW && e.getY() >= cy && e.getY() <= cy + cardH) {
						selected = i;
						SkinManager.setActiveSkin(SkinRegistry.ALL[i].getName());
						repaint();
					}
				}
				int bw = 160;
				int bh = 44;
				int bx = (w - bw) / 2;
				int by = getHeight() - 60;
				if(e.getX() >= bx && e.getX() <= bx + bw && e.getY() >= by && e.getY() <= by + bh) {
					if(onBack != null) {
						onBack.run();
					}
				}
				
			}
		});
		
	}
	public void setOnBack(Runnable onBack) {
		this.onBack = onBack;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		g2.setColor(new Color(255, 255, 255, 6));
		int tile = 24;
		for(int x = 0; x < w; x += tile) {
			g2.drawLine(x, 0, w, h);
		}
		for(int y = 0; y < h; y += tile) {
			g2.drawLine(0, y, w, y);
		}
		
		g2.setFont(FontManager.GAME_LARGE);
		FontMetrics fm = g2.getFontMetrics();
		String title = "SKINS";
		g2.setColor(Color.decode("#00cfff"));
		g2.drawString(title, (w - fm.stringWidth(title)) / 2, 50);
		
		int cardW = 118;
		int cardH = 135;
		int spacing = 12;
		int cols = 3;
		int totalW = cols * cardW + (cols - 1) * spacing;
		int startX = (w - totalW) / 2;
		int startY = 80;
		
		for(int i = 0; i < SkinRegistry.ALL.length; i++) {
			SnakeSkin skin = SkinRegistry.ALL[i];
			boolean unlocked = UnlockManager.isUnlocked(skin.getName());
			boolean isSelected = (i == selected) && unlocked;
			
			int col = i % cols;
			int row = i / cols;
			int cx = startX + col * (cardW + spacing);
			int cy = startY + row * (cardH + spacing);
			
			if(isSelected) {
				g2.setColor(Color.decode("#00cfff"));
				g2.fillRoundRect(cx - 2, cy - 2, cardW + 4, cardH + 4, 14, 14);
			}
			g2.setColor(unlocked ? Color.decode("#1e2030") : Color.decode("#13131f"));
			g2.fillRoundRect(cx, cy, cardW, cardH, 12, 12);
			
			int previewSize = 16;
			int px = cx + cardW / 2 - previewSize * 2;
			int py = cy + 20;
			for(int s = 4; s >= 0; s--) {
				if(!unlocked) {
					g2.setColor(s == 0 ? Color.decode("#333344") : Color.decode("#222233"));
				}else {
					g2.setColor(GamePanel.getSegmentColor(skin, s, s == 0));
				}
				g2.fillRoundRect(px + s * previewSize, py, previewSize - 2, previewSize - 2, 5, 5);
			}
			g2.setFont(FontManager.GAME_SMALL);
			fm = g2.getFontMetrics();
			g2.setColor(unlocked ? Color.WHITE : Color.decode("#444455"));
			String name = skin.getName();
			g2.drawString(name, cx + (cardW - fm.stringWidth(name)) / 2, cy + 60);
			
			g2.setFont(FontManager.GAME_SMALL);
			fm = g2.getFontMetrics();
			String status;
			if(!unlocked) {
				status = skin.getPrice() + "c";
			}else if(isSelected) {
				status = "ACTIVE";
				g2.setColor(Color.decode("#00cfff"));
			}else {
				status = "OWNED";
				g2.setColor(Color.decode("#2ed573"));
			}
			g2.drawString(status, cx + (cardW - fm.stringWidth(status)) / 2, cy + 80);
		}
		int bw = 160;
		int bh = 44;
		int bx = (w - bw) / 2;
		int by = h - 60;
		g2.setColor(Color.decode("#2f3542"));
		g2.fillRoundRect(bx, by, bw, bh, 12, 12);
		g2.setFont(FontManager.GAME);
		fm = g2.getFontMetrics();
		g2.setColor(Color.WHITE);
		String back = "BACK";
		g2.drawString(back, bx + (bw - fm.stringWidth(back)) / 2, by + (bh + fm.getAscent() - fm.getDescent()) / 2);
	}
	@Override
	public void addNotify() {
		super.addNotify();
		requestFocusInWindow();
	}
}
