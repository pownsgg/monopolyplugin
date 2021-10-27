package dev.powns.monopoly.domain.game.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class HotBarItem extends ItemStack {
	private final Consumer<Player> onRightClick;

	public HotBarItem(Material itemIcon, int buttonNumber, String label, ArrayList<String> description, Consumer<Player> onRightClick) {
		super(itemIcon, buttonNumber);

		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(label);
		meta.setLore(description);
		this.setItemMeta(meta);

		this.onRightClick = onRightClick;
	}

	public String getName() {
		return this.getItemMeta().getDisplayName();
	}

	public List<String> getDescription() {
		return this.getItemMeta().getLore();
	}

	public Consumer<Player> getOnRightClick() {
		return this.onRightClick;
	}
}
