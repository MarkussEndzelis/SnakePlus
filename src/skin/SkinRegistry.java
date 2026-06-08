package skin;

import java.awt.Color;

public class SkinRegistry {

	public static final SnakeSkin[] ALL = {
		new SnakeSkin("Classic", Color.decode("#2ed573"), Color.decode("#7bed9f"), Color.decode("#1abc9c")),
		new SnakeSkin("Neon", Color.decode("#ff00ff"), Color.decode("#da8fff"), Color.decode("#9b00cc")),
		new SnakeSkin("Fire", Color.decode("#ff4500"), Color.decode("#ff8c00"), Color.decode("#ff0000")),
		new SnakeSkin("Ice", Color.decode("#00cfff"), Color.decode("#a8e6ff"), Color.decode("#0080ff")),
		new SnakeSkin("Gold", Color.decode("#ffd700"), Color.decode("#ffe88a"), Color.decode("#b8860b")),
		new SnakeSkin("Retro", Color.decode("#ffffff"), Color.decode("#aaaaaa"), Color.decode("#555555")),
		new SnakeSkin("Toxic", Color.decode("#aaff00"), Color.decode("#ccff66"), Color.decode("#669900")),
		new SnakeSkin("Shadow", Color.decode("#9b59b6"), Color.decode("#6c3483"), Color.decode("#2c1654"))		
	};
}
