package skin;

import java.awt.Color;

public class SkinRegistry {

	public static final SnakeSkin[] ALL = {
		new SnakeSkin("Classic", Color.decode("#2ed573"), Color.decode("#7bed9f"), Color.decode("#1abc9c"), 0),
		new SnakeSkin("Neon", Color.decode("#ff00ff"), Color.decode("#da8fff"), Color.decode("#9b00cc"), 100),
		new SnakeSkin("Fire", Color.decode("#ff4500"), Color.decode("#ff8c00"), Color.decode("#ff0000"), 100),
		new SnakeSkin("Ice", Color.decode("#00cfff"), Color.decode("#a8e6ff"), Color.decode("#0080ff"), 250),
		new SnakeSkin("Gold", Color.decode("#ffd700"), Color.decode("#ffe88a"), Color.decode("#b8860b"), 250),
		new SnakeSkin("Retro", Color.decode("#ffffff"), Color.decode("#aaaaaa"), Color.decode("#555555"), 250),
		new SnakeSkin("Toxic", Color.decode("#aaff00"), Color.decode("#ccff66"), Color.decode("#669900"), 500),
		new SnakeSkin("Shadow", Color.decode("#9b59b6"), Color.decode("#6c3483"), Color.decode("#2c1654"), 500),
		new SnakeSkin("Lava", Color.decode("#ff4500"), Color.decode("#ff6a00"), Color.decode("#8b0000"), 1000),
		new SnakeSkin("Galaxy", Color.decode("#4a0080"), Color.decode("#9b59b6"), Color.decode("#1a0030"), 1500),
		new SnakeSkin("Rainbow", Color.decode("#ff0000"), Color.decode("#ff7700"), Color.decode("#ff0000"), 1000),
	};
	public static SnakeSkin getByName(String name) {
		for(SnakeSkin s : ALL) {
			if(s.getName().equals(name)) {
				return s;
			}
		}
		return ALL[0];
	}
}
