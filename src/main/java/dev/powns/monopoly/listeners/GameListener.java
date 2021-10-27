package dev.powns.monopoly.listeners;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.game.GameCell;
import dev.powns.monopoly.domain.game.GameStateEnum;
import dev.powns.monopoly.domain.player.PlayerState;
import dev.powns.monopoly.domain.player.TeamColorEnum;
import dev.powns.monopoly.managers.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();

		if (gameManager.getGameState() != GameStateEnum.PLAYING) {
			return;
		}

		Player player = e.getPlayer();
		PlayerState playerState = gameManager.getPlayerState(player.getUniqueId());

		if (playerState == PlayerState.PLAYING) {
			TeamColorEnum teamColorEnum = gameManager.getTeamByPlayerId(player.getUniqueId()).getTeamColor();

			if (teamColorEnum != null) {
				if (gameManager.getCurrentGame().getCurrentlyPlaying() == teamColorEnum) {
					return;
				}

				GameCell teamPosition = gameManager.getCurrentGame().getTeamPosition(teamColorEnum);
				Location movingTowards = e.getTo();

				if (movingTowards.getBlockX() < teamPosition.getMinX() || movingTowards.getBlockX() > teamPosition.getMaxX()
					|| movingTowards.getBlockZ() < teamPosition.getMinZ() || movingTowards.getBlockZ() > teamPosition.getMaxZ()) {

					e.setCancelled(true);
				}
			}
		}
	}
}
