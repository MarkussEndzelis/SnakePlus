package skin;

import java.awt.Color;

public class SnakeSkin {
	
	private final String name;
	private final Color headColor;
	private final Color bodyColor;
	private final Color outlineColor;
	public SnakeSkin(String name, Color headColor, Color bodyColor, Color outlineColor) {
		this.name = name;
		this.headColor = headColor;
		this.bodyColor = bodyColor;
		this.outlineColor = outlineColor;
	}
	public String getName() {
		return name;
	}
	public Color getHeadColor() {
		return headColor;
	}
	public Color getBodyColor() {
		return bodyColor;
	}
	public Color getOutlineColor() {
		return outlineColor;
	}
	
	
	
}
