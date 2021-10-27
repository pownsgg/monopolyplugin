package dev.powns.monopoly.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class Button {
	private final Material buttonIcon;
	private final int buttonNumber;
	private final BiConsumer<Player, Boolean> onClick;
	private String label;
	private ArrayList<String> description;

	public Button(String label, Material buttonIcon, int buttonNumber, BiConsumer<Player, Boolean> onClick) {
		this(label, new ArrayList<>(), buttonIcon, buttonNumber, onClick);
	}

	public Button(String label, ArrayList<String> description, Material buttonIcon, int buttonNumber, BiConsumer<Player, Boolean> onClick) {
		this.label = label;
		this.description = description;
		this.buttonIcon = buttonIcon;
		this.buttonNumber = buttonNumber;
		this.onClick = onClick;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Material getButtonIcon() {
		return this.buttonIcon;
	}

	public int getButtonNumber() {
		return this.buttonNumber;
	}

	public BiConsumer<Player, Boolean> getOnClick() {
		return this.onClick;
	}

	public ArrayList<String> getDescription() {
		return this.description;
	}
}
