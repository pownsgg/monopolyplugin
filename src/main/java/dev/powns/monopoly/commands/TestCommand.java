package dev.powns.monopoly.commands;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.game.GameStateEnum;
import dev.powns.monopoly.domain.player.Team;
import dev.powns.monopoly.gui.menu.DiceThrowingMenu;
import dev.powns.monopoly.managers.GameManager;
import dev.powns.monopoly.util.GhostPlayerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (!(commandSender instanceof Player)) {
			return true;
		}

		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
		Player player = (Player) commandSender;

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("start")) {
				if (MonopolyPlugin.getInstance().getGameManager().getGameState() == GameStateEnum.LOBBY) {
					MonopolyPlugin.getInstance().getGameManager().startGame();
				}
			} else if (args[0].equalsIgnoreCase("throwdice")) {
				Team playerTeam = gameManager.getTeamByPlayerId(player.getUniqueId());

				if (playerTeam == null) {
					return true;
				}

				if (gameManager.getCurrentGame().getCurrentlyPlaying() != playerTeam.getTeamColor()) {
					return true;
				}

				MonopolyPlugin.getInstance().getMenuManager().openGlobalMenu(new DiceThrowingMenu(player, System.currentTimeMillis()));
			} else if (args[0].equalsIgnoreCase("ghost")) {
				GhostPlayerUtil.ghostPlayer(player);
			}
		}
		return true;
	}
}