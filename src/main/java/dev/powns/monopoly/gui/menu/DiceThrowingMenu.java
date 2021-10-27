package dev.powns.monopoly.gui.menu;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.player.Team;
import dev.powns.monopoly.gui.Button;
import dev.powns.monopoly.gui.Menu;
import dev.powns.monopoly.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class DiceThrowingMenu extends Menu {
	private final Player openedBy;
	private final long firstOpened;
	private int lastShown1;
	private int lastShown2;

	public DiceThrowingMenu(Player throwingPlayer, long firstOpened) {
		this(throwingPlayer, firstOpened, -1, -1);
	}

	public DiceThrowingMenu(Player throwingPlayer, long firstOpened, int lastShown1, int lastShown2) {
		super(ChatColor.BLACK + "Throwing Dices...", 5, true);

		this.firstOpened = firstOpened;
		this.lastShown1 = lastShown1;
		this.lastShown2 = lastShown2;

		Material[] panes = new Material[]{Material.BLACK_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE};
		Random random = new Random();

		this.openedBy = throwingPlayer;
		BukkitRunnable diceSpinner = new BukkitRunnable() {
			@Override
			public void run() {
				if ((System.currentTimeMillis() - firstOpened) / 1000 > 6) {
					MonopolyPlugin.getInstance().getMenuManager().closeGlobalMenu();

					throwingPlayer.closeInventory();
					throwingPlayer.sendMessage("Dice 1 = " + lastShown1);
					throwingPlayer.sendMessage("Dice 2 = " + lastShown2);

					GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
					Team playerTeam = gameManager.getTeamByPlayerId(throwingPlayer.getUniqueId());

					gameManager.onDiceThrown(playerTeam, lastShown1, lastShown2);
					return;
				}

				MonopolyPlugin.getInstance().getMenuManager().updateGlobalMenu();
			}
		};

		long sleepTime = (long) ((1 / 220d) * (System.currentTimeMillis() - this.firstOpened));
		if (sleepTime < 3) {
			sleepTime = 3;
		}
		if (sleepTime > 18) {
			sleepTime = 18;
		}

		if (sleepTime < 18) {
			this.lastShown1 = new Random().nextInt(6 - 1 + 1) + 1;
			this.lastShown2 = new Random().nextInt(6 - 1 + 1) + 1;
		}

		for (int i = 0; i < this.getRows() * 9; i++) {
			if (i == 21) {
				DiceThrowingMenu.this.addButton(i, new Button("Dice 1", Material.NOTE_BLOCK, this.lastShown1, null));
				continue;
			}

			if (i == 23) {
				DiceThrowingMenu.this.addButton(i, new Button("Dice 2", Material.NOTE_BLOCK, this.lastShown2, null));
				continue;
			}

			int selectedPane = random.nextInt(panes.length);

			DiceThrowingMenu.this.addButton(i, new Button(ChatColor.GOLD + "*", panes[selectedPane], 1, null));
		}

		if (sleepTime < 18) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 2.0f, 1.0f);
			}
		}

		diceSpinner.runTaskLater(MonopolyPlugin.getInstance(), sleepTime);
	}

	@Override
	public Menu createUpdatedVersion() {
		return new DiceThrowingMenu(this.openedBy, this.firstOpened, this.lastShown1, this.lastShown2);
	}
}
