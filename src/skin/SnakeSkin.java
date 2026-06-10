package skin;

import java.awt.Color;

public class SnakeSkin {
	
	private final String name;
	private final Color headColor;
	private final Color bodyColor;
	private final Color outlineColor;
	private final int price;
	public SnakeSkin(String name, Color headColor, Color bodyColor, Color outlineColor, int price) {
		this.name = name;
		this.headColor = headColor;
		this.bodyColor = bodyColor;
		this.outlineColor = outlineColor;
		this.price = price;
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
	public int getPrice() {
		return price;
	}
	
	
	
}
