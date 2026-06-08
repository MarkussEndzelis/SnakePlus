package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import map.GameMap;
import skin.SnakeSkin;
import ui.GamePanel;
import ui.MapSelectScreen;
import ui.SkinSelectScreen;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Snake+");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			
			CardLayout cards = new CardLayout();
			JPanel root = new JPanel(cards);
			
			MapSelectScreen mapSelect = new MapSelectScreen();
			SkinSelectScreen skinSelect = new SkinSelectScreen();
			GamePanel gamePanel = new GamePanel();
			
			root.add(mapSelect, "MAP_SELECT");
			root.add(skinSelect, "SKIN_SELECT");
			root.add(gamePanel, "GAME");
			
			final GameMap[] chosenMap = {
				null	
			};
			
			mapSelect.setOnSelect(() -> {
				chosenMap[0] = mapSelect.getSelectedMap();
				cards.show(root, "SKIN_SELECT");
				skinSelect.requestFocusInWindow();
			});
			
			skinSelect.setOnSelect(() -> {
				SnakeSkin chosenSkin = skinSelect.getSelectedSkin();
				gamePanel.setSkin(chosenSkin);
				gamePanel.setPowerUpsEnabled(mapSelect.isPowerUpsEnabled());
				gamePanel.startGame(chosenMap[0]);
				cards.show(root, "GAME");
				gamePanel.requestFocusInWindow();
			});
			
			gamePanel.setOnBackToMenu(() -> {
				cards.show(root, "MAP_SELECT");
				mapSelect.requestFocusInWindow();
			});
			frame.add(root);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			mapSelect.requestFocusInWindow();
		});
}
}
