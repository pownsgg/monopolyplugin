package dev.powns.monopoly.gui;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {
	private final String menuTitle;
	protected final int rows;
	private final boolean animated;
	private final Map<Integer, Button> buttons = new HashMap<>();

	public Menu(String menuTitle, int rows) {
		this(menuTitle, rows, false);
	}

	public Menu(String menuTitle, int rows, boolean animated) {
		this.menuTitle = menuTitle;
		this.rows = rows;
		this.animated = animated;
	}

	public Menu addButton(int x, int y, Button button) {
		int index = (x - 1) + (y - 1);

		this.addButton(index, button);
		return this;
	}

	public Menu addButton(int index, Button button) {
		this.buttons.put(index, button);

		return this;
	}

	public String getMenuTitle() {
		return this.menuTitle;
	}

	public int getRows() {
		return this.rows;
	}

	public boolean isAnimated() {
		return this.animated;
	}

	public Map<Integer, Button> getButtons() {
		return this.buttons;
	}

	public void onClose() { }

	public abstract Menu createUpdatedVersion();
}
