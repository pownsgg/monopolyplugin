package dev.powns.monopoly.managers;

import dev.powns.monopoly.domain.game.GameCell;
import dev.powns.monopoly.domain.game.GameCellType;
import dev.powns.monopoly.domain.game.GameStateEnum;
import dev.powns.monopoly.domain.game.LandingReason;
import dev.powns.monopoly.domain.game.MonopolyGame;
import dev.powns.monopoly.domain.player.PlayerState;
import dev.powns.monopoly.domain.player.Team;
import dev.powns.monopoly.domain.player.TeamColorEnum;
import dev.powns.monopoly.domain.estate.RealEstate;
import dev.powns.monopoly.util.GhostPlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameManager {
	private GameStateEnum gameState = GameStateEnum.LOBBY;
	private final ArrayList<Team> teams = new ArrayList<>();
	private final Map<UUID, PlayerState> uuidToPlayerState = new HashMap<>();

	private MonopolyGame currentGame;

	public GameManager() {
		this.generateTeams();
	}

	public void onDiceThrown(Team teamThrown, int dice1, int dice2) {
		int amountThrown = dice1 + dice2;
		boolean doubleThrown = dice1 == dice2;

		if (doubleThrown) {
			int concurrentDoubles = this.currentGame.getConcurrentDoublesThrown();

			if (concurrentDoubles == 2) {
				// TODO: jailing.
				this.currentGame.nextTurn();
				return;
			}

			this.currentGame.setConcurrentDoublesThrown(concurrentDoubles + 1);
		}

		GameCell newGameCell = this.currentGame.moveTeam(teamThrown.getTeamColor(), amountThrown);

		RealEstate buyableEstate = newGameCell.getPurchaseableEstate();

		if (buyableEstate != null) {
			TeamColorEnum cellEstateOwner = buyableEstate.getEstateOwner();

			if (cellEstateOwner == null && buyableEstate.getPrice() <= teamThrown.getMoney()) {
				buyableEstate.setEstateOwner(teamThrown.getTeamColor());
				this.currentGame.getEstateOwned().get(teamThrown.getTeamColor()).add(buyableEstate);

				System.out.println(teamThrown.getTeamColor().getName() + " now owns " + buyableEstate.getName());
			} else if (cellEstateOwner != null && cellEstateOwner != teamThrown.getTeamColor()) {
				Team owningTeam = this.getTeamByColor(cellEstateOwner);

				int amountToPay = buyableEstate.getAmountToPay(LandingReason.DICE, amountThrown);

				teamThrown.setMoney(teamThrown.getMoney() - amountToPay);
				owningTeam.setMoney(owningTeam.getMoney() + amountToPay);

				System.out.println(cellEstateOwner.getName() + " received " + amountToPay + " from " + teamThrown.getTeamColor().getName());
			}
		} else if (newGameCell.getGameCellType() == GameCellType.PAY_200) {
			teamThrown.setMoney(teamThrown.getMoney() - 200);

			System.out.println(teamThrown.getTeamColor().getName() + " paid 200");
		} else if (newGameCell.getGameCellType() == GameCellType.PAY_100) {
			teamThrown.setMoney(teamThrown.getMoney() - 100);

			System.out.println(teamThrown.getTeamColor().getName() + " paid 100");
		}

		for (UUID teamMemberId : teamThrown.getTeamMembers()) {
			Player teamMember = Bukkit.getPlayer(teamMemberId);

			if (teamMember == null) {
				continue;
			}

			int newX = (newGameCell.getMinX() + newGameCell.getMaxX()) / 2 + 1;
			int newY = (newGameCell.getMinZ() + newGameCell.getMaxZ()) / 2;

			teamMember.teleport(new Location(Bukkit.getWorld("World"), newX, 111, newY));
		}

		if (!doubleThrown) {
			// TODO: move to correct place
			this.currentGame.nextTurn();
		}
	}

	public Team assignPlayerToTeam(Player player) {
		UUID playerId = player.getUniqueId();

		for (Team team : this.teams) {
			if (team.getTeamMembers().isEmpty()) {
				TeamColorEnum teamColor = team.getTeamColor();

				team.addPlayer(playerId);

				player.setDisplayName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
				player.setPlayerListName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
				player.setCustomName(teamColor.getTextColor() + player.getName() + ChatColor.RESET);
				return team;
			}
		}

		return null;
	}

	public Team getTeamByPlayerId(UUID playerId) {
		for (Team team : this.teams) {
			if (team.getTeamMembers().contains(playerId)) {
				return team;
			}
		}

		return null;
	}

	public Team getTeamByColor(TeamColorEnum teamColor) {
		for (Team team : this.teams) {
			if (team.getTeamColor() == teamColor) {
				return team;
			}
		}

		return null;
	}

	public void removePlayerFromCurrentTeam(Player player) {
		Team currentTeam = this.getTeamByPlayerId(player.getUniqueId());

		if (currentTeam != null) {
			currentTeam.removePlayer(player.getUniqueId());
			player.setDisplayName(ChatColor.RESET + player.getName());
			player.setPlayerListName(ChatColor.RESET + player.getName());
			player.setCustomName(ChatColor.RESET + player.getName());

//			ScoreboardCreator.updateBoard();
		}
	}

	public void reset() {
		this.resetPlayers();
//		this.generateTeams();
		this.setGameState(GameStateEnum.LOBBY);
	}

	public void startGame() {
		this.currentGame = new MonopolyGame();

		World gameWorld = Bukkit.getWorld("World");
		gameWorld.setTime(0);
		gameWorld.setPVP(false);
		gameWorld.setMonsterSpawnLimit(0);

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (this.getTeamByPlayerId(player.getUniqueId()) == null) {
				this.updatePlayerState(player.getUniqueId(), PlayerState.SPECTATING);
			} else {
				this.updatePlayerState(player.getUniqueId(), PlayerState.PLAYING);
			}
		}

		for (Team team : this.getActiveTeams()) {
			team.setMoney(1500);

			for (UUID memberId : team.getTeamMembers()) {
				Player member = Bukkit.getPlayer(memberId);
				if (member == null) {
					continue;
				}

				member.setGameMode(GameMode.SURVIVAL);

				// TODO: teleport to start
				member.teleport(new Location(Bukkit.getWorld("World"), -32, 111, -37));
			}
		}

		this.setGameState(GameStateEnum.PLAYING);
		GhostPlayerUtil.ghostTeam(this.getTeamByColor(this.currentGame.getCurrentlyPlaying()));

		Bukkit.broadcastMessage(ChatColor.GOLD + "Game started.");
	}

	public void resetPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.updatePlayerState(player.getUniqueId(), PlayerState.LOBBY);
			this.setGameState(GameStateEnum.LOBBY);
//			this.removePlayerFromCurrentTeam(player);

			player.setGameMode(GameMode.SURVIVAL);
			player.setHealth(20);
			player.setFoodLevel(20);

			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}

			player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 152, 0));

			player.getInventory().clear();
			player.setExp(0);
			player.setLevel(0);
		}
	}

	public void updatePlayerState(UUID playerId, PlayerState playerState) {
		this.uuidToPlayerState.put(playerId, playerState);
	}

	public PlayerState getPlayerState(UUID playerId) {
		return this.uuidToPlayerState.get(playerId);
	}

	public GameStateEnum getGameState() {
		return this.gameState;
	}

	public void setGameState(GameStateEnum gameState) {
		this.gameState = gameState;
	}

	public MonopolyGame getCurrentGame() {
		return this.currentGame;
	}

	public void generateTeams() {
		this.teams.clear();

		for (TeamColorEnum teamColorEnum : TeamColorEnum.values()) {
			this.teams.add(new Team(teamColorEnum));
		}
	}

	public List<Team> getTeams() {
		return this.teams;
	}

	public List<Team> getActiveTeams() {
		List<Team> result = new ArrayList<>();

		for (Team team : this.teams) {
			if (team.getTeamMembers().isEmpty()) {
				continue;
			}

			result.add(team);
		}

		return result;
	}
}
