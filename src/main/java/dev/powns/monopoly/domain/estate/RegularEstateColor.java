package dev.powns.monopoly.domain.estate;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum RegularEstateColor {
	BROWN(Material.BROWN_CONCRETE, ChatColor.DARK_GRAY),
	LIGHT_BLUE(Material.CYAN_CONCRETE, ChatColor.AQUA),
	PINK(Material.PINK_CONCRETE, ChatColor.LIGHT_PURPLE),
	ORANGE(Material.ORANGE_CONCRETE, ChatColor.GOLD),
	RED(Material.RED_CONCRETE, ChatColor.RED),
	YELLOW(Material.YELLOW_CONCRETE, ChatColor.YELLOW),
	GREEN(Material.GREEN_CONCRETE, ChatColor.GREEN),
	BLUE(Material.BLUE_CONCRETE, ChatColor.BLUE);

	Material blockMaterial;
	ChatColor nameColor;

	RegularEstateColor(Material blockMaterial, ChatColor nameColor) {
		this.blockMaterial = blockMaterial;
		this.nameColor = nameColor;
	}

	public ChatColor getNameColor() {
		return this.nameColor;
	}

	public Material getBlockMaterial() {
		return this.blockMaterial;
	}
}
