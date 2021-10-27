package dev.powns.monopoly.domain.game.hotbar;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.game.GameStateEnum;
import dev.powns.monopoly.gui.menu.DiceThrowingMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class Dice extends HotBarItem {
	public Dice() {
		super(Material.NOTE_BLOCK, 1, ChatColor.DARK_GREEN + "Biome Teleporter", new ArrayList<>(), player -> {
			if (MonopolyPlugin.getInstance().getGameManager().getGameState() == GameStateEnum.PLAYING) {
				MonopolyPlugin.getInstance().getMenuManager().openGlobalMenu(new DiceThrowingMenu(player, System.currentTimeMillis()));
			}
		});
	}
}
