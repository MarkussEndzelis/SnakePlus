package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import score.UsernameManager;
import javax.swing.JOptionPane;

import map.GameMap;
import score.SkinManager;
import skin.SnakeSkin;
import ui.GamePanel;
import ui.LeaderboardScreen;
import ui.MainMenuScreen;
import ui.MapSelectScreen;
import ui.ShopScreen;
import ui.SkinSelectScreen;
import ui.SkinsScreen;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			if (!UsernameManager.hasUsername()) {
				String name = null;
				while(name == null || name.trim().isEmpty()) {
					name = JOptionPane.showInputDialog(null, 
							"Enter a username for the leaderboard:",
							"Welcome to Snake+", JOptionPane.PLAIN_MESSAGE);
					if (name == null) {
						name = "Player";
						break;
					}
				}
				UsernameManager.setUsername(name.trim());
			}
			
			JFrame frame = new JFrame("Snake+");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			
			CardLayout cards = new CardLayout();
			JPanel root = new JPanel(cards);
			
			final MainMenuScreen mainMenu = new MainMenuScreen();
			MapSelectScreen mapSelect = new MapSelectScreen();
			SkinSelectScreen skinSelect = new SkinSelectScreen();
			GamePanel gamePanel = new GamePanel();
			ShopScreen shopScreen = new ShopScreen();
			SkinsScreen skinsScreen = new SkinsScreen();
			LeaderboardScreen leaderboardScreen = new LeaderboardScreen();
			
			root.add(shopScreen, "SHOP");
			root.add(mainMenu, "MAIN_MENU");
			root.add(mapSelect, "MAP_SELECT");
			root.add(skinSelect, "SKIN_SELECT");
			root.add(gamePanel, "GAME");
			root.add(skinsScreen, "SKINS");
			root.add(leaderboardScreen, "LEADERBOARD");
			
			final GameMap[] chosenMap = {
				null	
			};
			shopScreen.setOnBack(() -> {
				cards.show(root, "MAIN_MENU");
				SwingUtilities.invokeLater( () -> mainMenu.requestFocusInWindow());
			});
			
			
			
			leaderboardScreen.setOnBack(() -> {
				cards.show(root, "MAIN_MENU");
				SwingUtilities.invokeLater(() -> mainMenu.requestFocusInWindow());
			});
			
			mainMenu.setOnLeaderboard(() -> {
				leaderboardScreen.loadScores("Classic");
				cards.show(root, "LEADERBOARD");
				SwingUtilities.invokeLater(() -> leaderboardScreen.requestFocusInWindow());
			});
			
			mainMenu.setOnPlay(() -> {
				mainMenu.stopAnim();
				cards.show(root, "MAP_SELECT");
				mapSelect.requestFocusInWindow();
			});
			
			mapSelect.setOnSelect(() -> {
				chosenMap[0] = mapSelect.getSelectedMap();
				gamePanel.setSkin(SkinManager.getActiveSkin());
				gamePanel.setPowerUpsEnabled(mapSelect.isPowerUpsEnabled());
				gamePanel.startGame(chosenMap[0]);
				cards.show(root, "GAME");
				SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
			});
			
			skinSelect.setOnSelect(() -> {
				SnakeSkin chosenSkin = skinSelect.getSelectedSkin();
				gamePanel.setSkin(chosenSkin);
				gamePanel.setPowerUpsEnabled(mapSelect.isPowerUpsEnabled());
				gamePanel.startGame(chosenMap[0]);
				cards.show(root, "GAME");
				gamePanel.requestFocusInWindow();
			});
			mainMenu.setOnShop(() -> {
				cards.show(root, "SHOP");
				SwingUtilities.invokeLater( () -> shopScreen.requestFocusInWindow());
			});
			mainMenu.setOnSkins(() -> {
				cards.show(root, "SKINS");
				SwingUtilities.invokeLater(() -> skinsScreen.requestFocusInWindow());
			});
			skinsScreen.setOnBack(() -> {
				cards.show(root, "MAIN_MENU");
				SwingUtilities.invokeLater(() -> mainMenu.requestFocusInWindow());
			});
			
			gamePanel.setOnBackToMenu(() -> {
				mainMenu.startAnim();
				cards.show(root, "MAIN_MENU");
				SwingUtilities.invokeLater(() -> mainMenu.requestFocusInWindow());
			});
			frame.add(root);
			frame.pack();
			frame.setLocationRelativeTo(null);
			
			cards.show(root, "MAIN_MENU");
			mainMenu.requestFocusInWindow();
			
			frame.setVisible(true);
			
			
		});
}
}
