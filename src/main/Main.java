package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import map.GameMap;
import ui.GamePanel;
import ui.MapSelectScreen;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Snake+");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			
			CardLayout cards = new CardLayout();
			JPanel root = new JPanel(cards);
			
			MapSelectScreen mapSelect = new MapSelectScreen();
			GamePanel gamePanel = new GamePanel();
			
			root.add(mapSelect, "MAP_SELECT");
			root.add(gamePanel, "GAME");
			
			mapSelect.setOnSelect(() -> {
				GameMap chosen = mapSelect.getSelectedMap();
				gamePanel.startGame(chosen);
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
