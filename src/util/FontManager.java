package util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontManager {

	public static Font GAME;
	public static Font GAME_LARGE;
	public static Font GAME_SMALL;
	
	static {
		try {
			InputStream is = FontManager.class.getResourceAsStream("/PressStart2P-Regular.ttf");
			Font base = Font.createFont(Font.TRUETYPE_FONT, is);
			GAME = base.deriveFont(10f);
			GAME_LARGE = base.deriveFont(36f);
			GAME_SMALL = base.deriveFont(8f);
		}catch(FontFormatException | IOException e) {
			GAME = new Font("Monospaced", Font.BOLD, 10);
			GAME_LARGE = new Font("Monospaced", Font.BOLD, 36);
			GAME_SMALL = new Font("Monospaced", Font.BOLD, 8);
		}
	}
}
