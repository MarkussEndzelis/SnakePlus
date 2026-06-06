package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.GamePanel;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Snake+");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.add(new GamePanel());
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
	});
}
}
