package dev.kezuk.flodoria.enums;

public enum Color {
	DARK_GREEN(0),
	GREEN(5),
	YELLOW(10),
	GOLD(20),
	RED(30),
	DARK_RED(45),
	BLACK(75);

	private int value;

	Color(int value) {
		this.value = value;
	}

	public static Color getColorByValue(int value) {
		for (Color color : Color.values()) {
			if (color.value == value) {
				return color;
			}
		}
		return null;
	}

}
