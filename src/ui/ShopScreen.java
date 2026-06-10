package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

import javax.swing.JPanel;

import score.CoinManager;
import score.SkinRegistry;
import score.UnlockManager;
import skin.SnakeSkin;
import util.FontManager;

public class ShopScreen extends JPanel {

	private int selected = 0;
	private Runnable onBack;
	private String message = "";
	private int messageTick = 0;
	private Timer messageTimer;

	public ShopScreen() {
		setBackground(Color.decode("#0f0f1a"));
		setFocusable(true);

		messageTimer = new Timer(50, e -> {
			if (messageTick > 0) {
				messageTick--;
				repaint();
			}
		});
		messageTimer.start();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int len = SkinRegistry.ALL.length;
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					attemptPurchase();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (onBack != null) {
						onBack.run();
					}
				}
			}
		});
		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int w = getWidth();
				int cardW = 118;
				int cardH = 135;
				int spacing =12;
				int cols = 4;
				int totalW = cols * cardW + (cols - 1) * spacing;
				int startX = (w - totalW) / 2;
				int startY = 110;
				
				for(int i = 0; i < SkinRegistry.ALL.length; i++) {
					int col = i % cols;
					int row = i / cols;
					int cardX = startX + col * (cardW + spacing);
					int cardY = startY + row * (cardH + spacing);
					if(e.getX() >= cardX && e.getX() <= cardX + cardW && e.getY() >= cardY && e.getY() <=cardY + cardH) {
						selected = i;
						attemptPurchase();
						repaint();
						return;
					}
				}
				int bbw = 160;
				int bbh = 44;
				int bbx = (getWidth() - bbw) / 2;
				int bby = getHeight() - 58;
				if(e.getX() >= bbx && e.getX() <= bbx + bbw && e.getY() >= bby && e.getY() <= bby + bbh) {
					if(onBack != null) {
						onBack.run();
					}
				}
			}
		});

	}

	private void attemptPurchase() {
		SnakeSkin skin = SkinRegistry.ALL[selected];
		if (UnlockManager.isUnlocked(skin.getName())) {

			showMessage("Already owned!");
			return;
		}
		if (CoinManager.getCoins() < skin.getPrice()) {
			showMessage("Not enough coins!");
			return;
		}
		CoinManager.spend(skin.getPrice());
		UnlockManager.unlock(skin.getName());
		showMessage("Unlocked " + skin.getName() + "!");
		repaint();
	}

	private void showMessage(String msg) {
		message = msg;
		messageTick = 60;
		repaint();
	}

	public void setOnBack(Runnable onBack) {
		this.onBack = onBack;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		requestFocusInWindow();
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
		for (int x = 0; x < w; x += 24) {
			g2.drawLine(x, 0, x, h);
		}
		for (int y = 0; y < h; y += 24) {
			g2.drawLine(0, y, w, y);
		}
		g2.setFont(FontManager.GAME_LARGE);
		FontMetrics fm = g2.getFontMetrics();
		String title = "SHOP";
		g2.setColor(Color.decode("#ffd700"));
		g2.drawString(title, (w - fm.stringWidth(title)) / 2, 60);

		g2.setFont(FontManager.GAME);
		fm = g2.getFontMetrics();
		String coins = "COINS: " + CoinManager.getCoins();
		g2.setColor(Color.decode("#ffd700"));
		g2.drawString(coins, (w - fm.stringWidth(coins)) / 2, 90);

		int cardW = 118;
		int cardH = 135;
		int spacing = 12;
		int cols = 4;
		int totalW = cols * cardW + (cols - 1) * spacing;
		int startX = (w - totalW) / 2;
		int startY = 110;

		for (int i = 0; i < SkinRegistry.ALL.length; i++) {
			SnakeSkin skin = SkinRegistry.ALL[i];
			int col = i % cols;
			int row = i / cols;
			int cardX = startX + col * (cardW + spacing);
			int cardY = startY + row * (cardH + spacing);
			boolean isSelected = i == selected;
			boolean owned = UnlockManager.isUnlocked(skin.getName());

			g2.setColor(isSelected ? Color.decode("#2f3542") : Color.decode("#1e2030"));
			g2.fillRoundRect(cardX, cardY, cardW, cardH, 14, 14);

			Color borderColor = owned ? Color.decode("#2ed573")
					: isSelected ? Color.decode("#ffd700") : Color.decode("#576574");
			g.setColor(borderColor);
			g2.setStroke(new BasicStroke(isSelected ? 3 : 1));
			g2.drawRoundRect(cardX, cardY, cardW, cardH, 14, 14);

			drawSkinPreview(g2, skin, cardX + 8, cardY + 10, cardW - 16, 80, owned);

			g2.setFont(FontManager.GAME_SMALL);
			fm = g2.getFontMetrics();
			g2.setColor(owned ? Color.decode("#2ed573") : isSelected ? Color.decode("#ffd700") : Color.WHITE);
			String name = skin.getName();
			g2.drawString(name, cardX + (cardW - fm.stringWidth(name)) / 2, cardY + 108);

			if (owned) {
				g2.setColor(Color.decode("#2ed573"));
				String ownedTxt = "OWNED";
				g2.drawString(ownedTxt, cardX + (cardW - fm.stringWidth(ownedTxt)) / 2, cardY + 125);
			} else {
				g2.setColor(Color.decode("#ffd700"));
				String price = skin.getPrice() + "c";
				g2.drawString(price, cardX + (cardW - fm.stringWidth(price)) / 2, cardY + 125);
			}
			if (isSelected && !owned) {
				g2.setColor(Color.decode("#ffd700"));
				String buy = "ENTER=BUY";
				g2.drawString(buy, cardX + (cardW - fm.stringWidth(buy)) / 2, cardY + 148);
			}
		}
		if (messageTick > 0) {
			g2.setFont(FontManager.GAME);
			fm = g2.getFontMetrics();
			float alpha = Math.min(1f, messageTick / 20f);
			g2.setColor(new Color(1f, 1f, 1f, alpha));
			g2.drawString(message, (w - fm.stringWidth(message)), h - 60);
		}
		int bbw = 160;
		int bbh = 44;
		int bbx = (w - bbw) / 2;
		int bby = h - 58;
		g.setColor(Color.decode("#2f3542"));
		g2.fillRoundRect(bbx, bby, bbw, bbh, 12, 12);
		g2.setFont(FontManager.GAME);
		fm = g2.getFontMetrics();
		g2.setColor(Color.WHITE);
		String backLabel = "BACK";
		g2.drawString(backLabel, bbx + (bbw - fm.stringWidth(backLabel)) / 2, bby + (bbh + fm.getAscent() - fm.getDescent()) / 2);
	}

	private void drawSkinPreview(Graphics2D g, SnakeSkin skin, int x, int y, int w, int h, boolean owned) {
		g.setColor(Color.decode("#0f0f1a"));
		g.fillRoundRect(x, y, w, h, 8, 8);

		if (!owned) {
			g.setColor(Color.decode("#576574"));
			g.fillRoundRect(x + w / 2 - 10, y + h / 2 - 12, 20, 16, 4, 4);
			g.setColor(Color.decode("#576574"));
			g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawArc(x + w / 2 - 7, y + h / 2 - 20, 14, 14, 0, 180);
			return;
		}
		int tile = 12;
		int[][] shape = { { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 1 }, { 5, 1 }, { 6, 1 }, { 6, 2 }, { 6, 3 }, { 5, 3 },
				{ 4, 3 }, { 3, 3 }, { 2, 3 }, { 1, 3 }, { 1, 4 }, { 1, 5 } };

		for (int i = 0; i < shape.length; i++) {
			int px = x + shape[i][0] * tile;
			int py = y + shape[i][1] * tile;
			if (i == 0) {
				g.setColor(GamePanel.getSegmentColor(skin, 0, true));
				g.fillRoundRect(px, py, tile - 1, tile - 1, 4, 4);
			} else {
				g.setColor(GamePanel.getSegmentColor(skin, i, false));
				g.fillRoundRect(px + 1, py + 1, tile - 3, tile - 3, 3, 3);
			}
		}
	}

}