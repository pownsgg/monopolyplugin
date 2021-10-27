package dev.powns.monopoly.domain.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum TeamColorEnum {

	RED("Red", ChatColor.RED, Material.REDSTONE_BLOCK),
	BLUE("Blue", ChatColor.BLUE, Material.LAPIS_BLOCK),
	GREEN("Green", ChatColor.GREEN, Material.EMERALD_BLOCK),
	YELLOW("Yellow", ChatColor.YELLOW, Material.SPONGE),
	PURPLE("Purple", ChatColor.DARK_PURPLE, Material.PURPUR_BLOCK),
	PINK("Pink", ChatColor.LIGHT_PURPLE, Material.PINK_WOOL),
	AQUA("Aqua", ChatColor.AQUA, Material.BLUE_ICE);

	private String name;
	private ChatColor textColor;
	private Material teamBlock;

	TeamColorEnum(String name, ChatColor textColor, Material teamBlock) {
		this.name = name;
		this.textColor = textColor;
		this.teamBlock = teamBlock;
	}

	public String getName() {
		return name;
	}

	public ChatColor getTextColor() {
		return textColor;
	}

	public Material getTeamBlock() {
		return teamBlock;
	}
}
