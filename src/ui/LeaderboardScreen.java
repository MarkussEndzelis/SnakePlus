package ui;

import score.LeaderboardManager;
import engine.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LeaderboardScreen extends JPanel {
	private Runnable onBack;
	private String mapName = "Classic";
	private java.util.List<int[]> scores = new java.util.ArrayList<>();
	private java.util.List<String> usernames = new java.util.ArrayList<>();
	private boolean loading = false;
	
	public LeaderboardScreen() {
		setBackground(Color.decode("#0f0f1a"));
		setFocusable(true);
		setPreferredSize(new Dimension(
				GameState.COLS * GamePanel.TILE,
				GameState.ROWS * GamePanel.TILE
				));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (onBack != null) {
						onBack.run();
					}
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int w = getWidth();
		        int h = getHeight();

		        if (e.getX() >= 20 && e.getX() <= 120 && e.getY() >= h - 50 && e.getY() <= h - 20) {
		            if (onBack != null) {
		                onBack.run();
		            }
		        }
		        if(e.getX() >= w/2 - 235 && e.getX() <= w/2 - 125 && e.getY() >= 55 && e.getY() <= 85) {
		            loadScores("Classic");
		        }
		        if(e.getX() >= w/2 - 115 && e.getX() <= w/2 - 5 && e.getY() >= 55 && e.getY() <= 85) {
		            loadScores("Obstacle map");
		        }
		        if(e.getX() >= w/2 + 5 && e.getX() <= w/2 + 115 && e.getY() >= 55 && e.getY() <= 85) {
		            loadScores("Maze");
		        }
		        if(e.getX() >= w/2 + 125 && e.getX() <= w/2 + 235 && e.getY() >= 55 && e.getY() <= 85) {
		        	loadScores("Shrinking Arena");
		        }
		    }
		});
	}
	
	public void loadScores(String map) {
		this.mapName = map;
		this.scores.clear();
		this.usernames.clear();
		this.loading = true;
		repaint();
		
		new Thread(() -> {
			String raw = LeaderboardManager.fetchTopScores(map);
			scores.clear();
			usernames.clear();
			if (raw != null) {
				try {
					parseResults(raw);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			loading = false;
			SwingUtilities.invokeLater(() -> repaint());
		}).start();
	}
	
	private void parseResults(String raw) {
		int index = 0;
		while(true) {
			int docIdx = raw.indexOf("\"document\"", index);
			if (docIdx == -1) break;
			
			String user = extractStringValue(raw, "username", docIdx);
			String scoreStr = extractIntegerValue(raw, "score", docIdx);
			
			
			if (user != null && scoreStr != null) {
				usernames.add(user);
				scores.add(new int[] {Integer.parseInt(scoreStr)});
			}
			index = docIdx + 10;
		}
	}
	
	private String extractStringValue(String raw, String fieldName, int fromIndex) {
		String key = "\"" + fieldName + "\"";
		int keyIdx = raw.indexOf(key, fromIndex);
		if (keyIdx == -1) return null;
		int valIdx = raw.indexOf("\"stringValue\"", keyIdx);
		if (valIdx == -1) return null;
		int colonIdx = raw.indexOf(':', valIdx);
		int firstQuote = raw.indexOf('"', colonIdx + 1);
		int secondQuote = raw.indexOf('"', firstQuote + 1);
		if (firstQuote == -1 || secondQuote == -1) return null;
		return raw.substring(firstQuote + 1, secondQuote);
	}
	
	private String extractIntegerValue(String raw, String fieldName, int fromIndex) {
		String key = "\"" + fieldName + "\"";
		int keyIdx = raw.indexOf(key, fromIndex);
		if (keyIdx == -1) return null;
		int valIdx = raw.indexOf("\"integerValue\"");
		if (valIdx == -1) return null;
		int colonIdx = raw.indexOf(':', valIdx);
		int firstQuote = raw.indexOf('"', colonIdx + 1);
		int secondQuote = raw.indexOf('"', firstQuote + 1);
		if (firstQuote != -1 && secondQuote != -1 && secondQuote > firstQuote) {
			return raw.substring(firstQuote + 1, secondQuote);
		}
		
		int end = colonIdx + 1;
		while(end < raw.length() && (Character.isDigit(raw.charAt(end)) || raw.charAt(end) == '-' || raw.charAt(end) == ' ')) {
			end++;
		}
		return raw.substring(colonIdx + 1, end).trim();
	}
	
	public void setOnBack(Runnable onBack) {
		this.onBack = onBack;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();
		
		g2.setColor(Color.decode("#2ed573"));
		g2.setFont(new Font("Arial", Font.BOLD, 32));
		FontMetrics fm = g2.getFontMetrics();
		String title = "GLOBAL LEADERBOARD";
		g2.drawString(title, (w - fm.stringWidth(title)) / 2, 45);
		drawMapButton(g2, "Classic", w/2 - 235, 55, mapName.equals("Classic"));
		drawMapButton(g2, "Obstacle Map", w/2 - 115, 55, mapName.equals("Obstacle map"));
		drawMapButton(g2, "Maze", w/2 + 5, 55, mapName.equals("Maze"));
		drawMapButton(g2, "Shrinking Arena", w/2 + 125, 55, mapName.equals("Shrinking Arena"));
		
		g2.setColor(Color.decode("#f2f3542"));
		g2.fillRect(40, 95, w - 80, 2);
		
		if (loading) {
			g2.setColor(Color.decode("#a4b0be"));
			g2.setFont(new Font("Arial", Font.PLAIN, 16));
			g2.drawString("Loading...", w/2 - 40, h/2);
		}else if (scores.isEmpty()) {
			g2.setColor(Color.decode("#576574"));
			g2.setFont(new Font("Arial", Font.PLAIN, 16));
			String msg = "No scores yet - be the first!";
			fm = g2.getFontMetrics();
			g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h/2);
		}else {
			g2.setFont(new Font("Arial", Font.BOLD, 13));
			g2.setColor(Color.decode("#a4b0be"));
			g2.drawString("#", 60, 120);
			g2.drawString("PLAYER", 100, 120);
			g2.drawString("SCORE", w - 100, 120);
			g2.fillRect(40, 125, w - 80, 1);
			
			for (int i = 0; i < scores.size(); i++) {
				int rowY = 148 + i * 36;
				if ( i == 0) {
					g2.setColor(Color.decode("#ffd700"));
				}else if (i == 1) {
					g2.setColor(Color.decode("#c0c0c0"));
				}else if (i == 2) {
					g2.setColor(Color.decode("#cd7f32"));
				}else {
					g2.setColor(Color.WHITE);
				}
				
				g2.setFont(new Font("Arial", Font.BOLD, 15));
				g2.drawString((i + 1) + ".", 60,  rowY);
				g2.drawString(usernames.get(i), 100, rowY);
				
				fm = g2.getFontMetrics();
				String sc = String.valueOf(scores.get(i)[0]);
				g2.drawString(sc,  w - 60 - fm.stringWidth(sc), rowY);
				
				g2.setColor(new Color(255, 255, 255, 15));
				g2.fillRect(40, rowY + 6, w - 80, 1);
			}
		}
		
		g2.setColor(Color.decode("#2f3542"));
		g2.fillRoundRect(20, h - 50, 100, 30, 10, 10);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 13));
		g2.drawString("<- BACK", 32, h - 29);
	}
	
	private void drawMapButton(Graphics2D g, String label, int x, int y, boolean active) {
		g.setColor(active ? Color.decode("#2ed573") : Color.decode("#2f3542"));
		g.fillRoundRect(x, y, 110, 30, 8, 8);
		g.setColor(active ? Color.decode("#0f0f1a") : Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(label, x + (110 - fm.stringWidth(label)) / 2, y + 20);
	}
}



















