package dev.powns.monopoly.commands;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.MonopolyGame;
import dev.powns.monopoly.domain.Team;
import dev.powns.monopoly.managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (!(commandSender instanceof Player)) {
			return true;
		}

		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();

		Player player = (Player) commandSender;
		Team playerTeam = gameManager.getTeamByPlayerId(player.getUniqueId());

		if (playerTeam == null) {
			return true;
		}

		gameManager.setCurrentGame(new MonopolyGame());

		if (gameManager.getCurrentGame().getCurrentlyPlaying() != playerTeam.getTeamColor()) {
			return true;
		}

		int thrownNumber = new Random().nextInt(12 - 2 + 1) + 2;

		if (args.length > 0) {
			try {
				thrownNumber = Integer.parseInt(args[0]);
			} catch (Exception ex) {

			}
		}

		gameManager.onDiceThrown(playerTeam, thrownNumber);
		return true;
	}
}
