package dev.powns.monopoly.listeners;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.GameStateEnum;
import dev.powns.monopoly.domain.PlayerState;
import dev.powns.monopoly.domain.Team;
import dev.powns.monopoly.domain.TeamColorEnum;
import dev.powns.monopoly.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
		PlayerState playerState = gameManager.getPlayerState(e.getPlayer().getUniqueId());
		Player player = e.getPlayer();

//		ScoreboardCreator.updateBoard();

		if (playerState == PlayerState.PLAYING) {
			Team team = gameManager.getTeamByPlayerId(player.getUniqueId());
			TeamColorEnum teamColor = team.getTeamColor();

			player.setDisplayName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
			player.setPlayerListName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
			player.setCustomName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
			return;
		}

		if (playerState == null) {
			switch (gameManager.getGameState()) {
				case LOBBY: {
					Team joinedTeam = gameManager.getTeamByPlayerId(player.getUniqueId());
					if (joinedTeam == null) {
						joinedTeam = gameManager.assignPlayerToTeam(player);
					}

					if (joinedTeam == null) {
						gameManager.updatePlayerState(e.getPlayer().getUniqueId(), PlayerState.SPECTATING);
					} else {
						gameManager.updatePlayerState(e.getPlayer().getUniqueId(), PlayerState.LOBBY);
					}

					break;
				}
				case PLAYING: {
					gameManager.updatePlayerState(e.getPlayer().getUniqueId(), PlayerState.SPECTATING);
					player.setGameMode(GameMode.SPECTATOR);
				}
			}
		}

		if (gameManager.getGameState() == GameStateEnum.LOBBY) {
			player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 152, 0));
		}

		if (playerState == PlayerState.SPECTATING) {
			player.setGameMode(GameMode.SPECTATOR);
		}

		if (gameManager.getActiveTeams().size() > 0) {
			gameManager.startGame();
		}
	}

	@EventHandler
	public void onPhantomSpawn(EntitySpawnEvent e) {
		if (e.getEntityType() == EntityType.PHANTOM) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (MonopolyPlugin.getInstance().getGameManager().getGameState() == GameStateEnum.LOBBY) {
			MonopolyPlugin.getInstance().getGameManager().removePlayerFromCurrentTeam(e.getPlayer());
//			ScoreboardCreator.updateBoard();
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBuild(BlockPlaceEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		e.setFoodLevel(20);
	}
}
