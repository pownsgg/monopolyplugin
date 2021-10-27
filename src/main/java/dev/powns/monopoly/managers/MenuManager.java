package dev.powns.monopoly.managers;

import dev.powns.monopoly.gui.Button;
import dev.powns.monopoly.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager implements Listener {
	private final Map<UUID, Menu> openedMenuByPlayer = new HashMap<>();
	private Menu globalMenu;

	public void openGlobalMenu(Menu menu) {
		this.globalMenu = menu;

		Inventory menuInventory = Bukkit.createInventory(null, menu.getRows() * 9, menu.getMenuTitle());
		this.fillInventory(menuInventory, menu);

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.openInventory(menuInventory);
			this.openedMenuByPlayer.put(player.getUniqueId(), menu);
		}
	}

	public void openMenu(Player player, Menu menu) {
		Inventory menuInventory = Bukkit.createInventory(null, menu.getRows() * 9, menu.getMenuTitle());

		this.fillInventory(menuInventory, menu);
		player.openInventory(menuInventory);

		this.openedMenuByPlayer.put(player.getUniqueId(), menu);
	}

	public void updateGlobalMenu() {
		if (this.globalMenu == null) {
			return;
		}

		this.globalMenu = this.globalMenu.createUpdatedVersion();
		Inventory inventory = null;

		for (Player player : Bukkit.getOnlinePlayers()) {
			InventoryView inventoryView = player.getOpenInventory();
			inventory = inventoryView.getTopInventory();

			if (!inventoryView.getTitle().equals(this.globalMenu.getMenuTitle())) {
				inventory = null;
				continue;
			}

			inventory.clear();
			break;
		}

		if (inventory == null) {
			inventory = Bukkit.createInventory(null, this.globalMenu.getRows() * 9, this.globalMenu.getMenuTitle());
		}

		this.fillInventory(inventory, this.globalMenu);

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (this.getOpenedMenuByPlayer(player.getUniqueId()) == null) {
				player.openInventory(inventory);
			}

			this.openedMenuByPlayer.put(player.getUniqueId(), this.globalMenu);
		}
	}

	public void updateMenu(Player player) {
		Menu opened = this.openedMenuByPlayer.get(player.getUniqueId());
		InventoryView inventoryView = player.getOpenInventory();

		if (opened != null) {
			Menu updatedMenu = opened.createUpdatedVersion();
			Inventory inventory = inventoryView.getTopInventory();

			inventory.clear();
			this.fillInventory(inventory, updatedMenu);

			this.openedMenuByPlayer.put(player.getUniqueId(), updatedMenu);
		}
	}

	public Menu getOpenedMenuByPlayer(UUID playerId) {
		return this.openedMenuByPlayer.get(playerId);
	}

	private void fillInventory(Inventory inventory, Menu menu) {
		for (Map.Entry<Integer, Button> buttonEntry : menu.getButtons().entrySet()) {
			int index = buttonEntry.getKey();
			Button button = buttonEntry.getValue();

			ItemStack buttonStack = new ItemStack(button.getButtonIcon(), button.getButtonNumber());
			ItemMeta buttonMeta = buttonStack.getItemMeta();

			buttonMeta.setDisplayName(button.getLabel());
			buttonMeta.setLore(button.getDescription());

			buttonStack.setItemMeta(buttonMeta);

			inventory.setItem(index, buttonStack);
		}
	}

	@EventHandler
	public void onButtonClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Menu openedMenu = this.openedMenuByPlayer.get(player.getUniqueId());

		if (this.openedMenuByPlayer.get(player.getUniqueId()) == null) {
			return;
		}

		event.setCancelled(true);

		if (event.getSlotType() != InventoryType.SlotType.CONTAINER) {
			return;
		}

		Inventory clickedInventory = event.getClickedInventory();
		int index = event.getSlot();

		ItemStack clickedItem = clickedInventory.getItem(index);

		if (clickedItem != null) {
			Button clicked = openedMenu.getButtons().get(index);

			if (clicked != null) {
				if (clicked.getOnClick() == null) {
					return;
				}

				clicked.getOnClick().accept(player, event.isShiftClick());
			}
		}
	}

	@EventHandler
	public void onMenuClosed(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Menu closingMenu = this.openedMenuByPlayer.get(player.getUniqueId());

		if (closingMenu != null) {
			this.openedMenuByPlayer.remove(player.getUniqueId());
			closingMenu.onClose();
		}
	}

	public void closeGlobalMenu() {
		this.globalMenu = null;
	}
}
