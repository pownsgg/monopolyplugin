package dev.powns.monopoly.domain;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.estate.CompanyEstate;
import dev.powns.monopoly.domain.estate.RealEstate;
import dev.powns.monopoly.domain.estate.RegularEstate;
import dev.powns.monopoly.domain.estate.RegularEstateColor;
import dev.powns.monopoly.domain.estate.TrainStation;
import dev.powns.monopoly.managers.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonopolyGame {
	private final List<GameCell> board = new ArrayList<>();
	private final Map<TeamColorEnum, List<RealEstate>> estateOwned = new HashMap<>();
	private final Map<TeamColorEnum, Integer> teamLocations = new HashMap<>();

	private int housesInStock = 10;
	private int hotelsInStock = 4;

	private TeamColorEnum currentlyPlaying;
	private int concurrentDoublesThrown = 0;

	public MonopolyGame() {
		this.currentlyPlaying = MonopolyPlugin.getInstance().getGameManager().getTeams().get(0).getTeamColor();

		this.initBoard();
		MonopolyPlugin.getInstance().getBoardGenerator().generateBoard(this.board);
	}

	public void nextTurn() {
		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
		List<Team> teams = gameManager.getActiveTeams();

		Team currentTeam = gameManager.getTeamByColor(this.currentlyPlaying);
		int currentTeamIndex = teams.indexOf(currentTeam);

		if (currentTeamIndex + 1 == teams.size()) {
			currentTeamIndex = 0;
		} else {
			currentTeamIndex++;
		}

		this.currentlyPlaying = teams.get(currentTeamIndex).getTeamColor();
		this.concurrentDoublesThrown = 0;

		System.out.println(this.currentlyPlaying.getName() + " is next.");
	}

	public GameCell moveTeam(TeamColorEnum teamColorEnum, int change) {
		int currentPosition = this.teamLocations.get(teamColorEnum);
		int maxPosition = this.board.size();

		System.out.println(currentPosition);
		System.out.println(maxPosition);
		System.out.println(currentPosition + change);

		if (currentPosition + change >= maxPosition) {
			currentPosition = (currentPosition + change) - maxPosition;
		} else if (currentPosition + change < 0) {
			currentPosition = maxPosition + (currentPosition + change);
		} else {
			currentPosition += change;
		}

		this.teamLocations.put(teamColorEnum, currentPosition);

		return this.board.get(currentPosition);
	}

	public boolean fullStreetOwned(TeamColorEnum teamColorEnum, RegularEstateColor regularEstateColor) {
		List<RealEstate> estateOwned = this.estateOwned.get(teamColorEnum);
		int amountOwned = 0;
		boolean onlyTwoNeeded = regularEstateColor == RegularEstateColor.BROWN || regularEstateColor == RegularEstateColor.BLUE;

		for (RealEstate realEstate : estateOwned) {
			if (realEstate instanceof RegularEstate) {
				amountOwned += ((RegularEstate) realEstate).getRegularEstateColor() == regularEstateColor ? 1 : 0;
			}
		}

		return onlyTwoNeeded ? amountOwned == 2 : amountOwned == 3;
	}

	public TeamColorEnum getEstateOwner(int cell) {
		return this.board.get(cell).getPurchaseableEstate().getEstateOwner();
	}

	public int getHousesInStock() {
		return this.housesInStock;
	}

	public void setHousesInStock(int housesInStock) {
		this.housesInStock = housesInStock;
	}

	public int getHotelsInStock() {
		return this.hotelsInStock;
	}

	public void setHotelsInStock(int hotelsInStock) {
		this.hotelsInStock = hotelsInStock;
	}

	public TeamColorEnum getCurrentlyPlaying() {
		return this.currentlyPlaying;
	}

	public void setCurrentlyPlaying(TeamColorEnum currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
	}

	public int getConcurrentDoublesThrown() {
		return this.concurrentDoublesThrown;
	}

	public void setConcurrentDoublesThrown(int concurrentDoublesThrown) {
		this.concurrentDoublesThrown = concurrentDoublesThrown;
	}

	public List<GameCell> getBoard() {
		return this.board;
	}

	public Map<TeamColorEnum, List<RealEstate>> getEstateOwned() {
		return this.estateOwned;
	}

	private void initBoard() {
		this.board.clear();

		this.board.add(new GameCell(GameCellType.START, -38, -45, -29, -36));

		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.BROWN, "Brown 1", 60, new int[]{6, 30, 50, 100, 250, 400}), -27, -45, -22, -36));
		this.board.add(new GameCell(GameCellType.COMPANY_CHEST, -20, -45, -15, -36));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.BROWN, "Brown 2", 100, new int[]{6, 30, 50, 100, 250, 400}), -13, -45, -8, -36));
		this.board.add(new GameCell(GameCellType.PAY_200, -6, -45, -1, -36));
		this.board.add(new GameCell(new TrainStation("Train 1", 200), 1, -45, 6, -36));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.LIGHT_BLUE, "Light Blue 1", 100, new int[]{6, 30, 50, 100, 250, 400}), 8, -45, 13, -36));
		this.board.add(new GameCell(GameCellType.CHANCE, 15, -45, 20, -36));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.LIGHT_BLUE, "Light Blue 2", 150, new int[]{6, 30, 50, 100, 250, 400}), 22, -45, 27, -36));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.LIGHT_BLUE, "Light Blue 3", 170, new int[]{6, 30, 50, 100, 250, 400}), 29, -45, 34, -36));

		this.board.add(new GameCell(GameCellType.JAIL, 36, -45, 45, -36));

		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.PINK, "Pink 1", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, -34, 45, -29));
		this.board.add(new GameCell(GameCellType.BUYABLE, new CompanyEstate("Company 1", 150), 36, -27, 45, -22));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.PINK, "Pink 2", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, -20, 45, -15));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.PINK, "Pink 3", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, -13, 45, -8));
		this.board.add(new GameCell(new TrainStation("Train 2", 200), 36, -6, 45, -1));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.ORANGE, "Orange 1", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, 1, 45, 6));
		this.board.add(new GameCell(GameCellType.COMPANY_CHEST, 36, 8, 45, 13));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.ORANGE, "Orange 2", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, 15, 45, 20));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.ORANGE, "Orange 3", 170, new int[]{6, 30, 50, 100, 250, 400}), 36, 22, 45, 27));

		this.board.add(new GameCell(GameCellType.FREE_PARK, 36, 29, 45, 38));

		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.RED, "Red 1", 170, new int[]{6, 30, 50, 100, 250, 400}), 29, 29, 34, 38));
		this.board.add(new GameCell(GameCellType.CHANCE, 22, 29, 27, 38));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.RED, "Red 2", 170, new int[]{6, 30, 50, 100, 250, 400}), 15, 29, 20, 38));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.RED, "Red 3", 170, new int[]{6, 30, 50, 100, 250, 400}), 8, 29, 13, 38));
		this.board.add(new GameCell(new TrainStation("Train 3", 200), 1, 29, 6, 38));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.YELLOW, "Yellow 1", 170, new int[]{6, 30, 50, 100, 250, 400}), -6, 29, -1, 38));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.YELLOW, "Yellow 2", 170, new int[]{6, 30, 50, 100, 250, 400}), -13, 29, -8, 38));
		this.board.add(new GameCell(GameCellType.BUYABLE, new CompanyEstate("Company 2", 150), -20, 29, -15, 38));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.YELLOW, "Yellow 3", 170, new int[]{6, 30, 50, 100, 250, 400}), -27, 29, -22, 38));

		this.board.add(new GameCell(GameCellType.ARREST, -38, 29, -29, 38));

		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.GREEN, "Green 1", 170, new int[]{6, 30, 50, 100, 250, 400}), -38, 22, -29, 27));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.GREEN, "Green 2", 170, new int[]{6, 30, 50, 100, 250, 400}), -38, 15, -29, 20));
		this.board.add(new GameCell(GameCellType.COMPANY_CHEST, -38, 8, -29, 13));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.GREEN, "Green 3", 170, new int[]{6, 30, 50, 100, 250, 400}), -38, 1, -29, 6));
		this.board.add(new GameCell(new TrainStation("Train 4", 200), -38, -6, -29, -1));
		this.board.add(new GameCell(GameCellType.CHANCE, -38, -13, -29, -8));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.BLUE, "Blue 1", 170, new int[]{6, 30, 50, 100, 250, 400}), -38, -20, -29, -15));
		this.board.add(new GameCell(GameCellType.PAY_100, -38, -27, -29, -22));
		this.board.add(new GameCell(new RegularEstate(RegularEstateColor.BLUE, "Blue 2", 170, new int[]{6, 30, 50, 100, 250, 400}), -38, -34, -29, -29));

		for (Team team : MonopolyPlugin.getInstance().getGameManager().getActiveTeams()) {
			this.estateOwned.put(team.getTeamColor(), new ArrayList<>());
			this.teamLocations.put(team.getTeamColor(), 0);
		}
	}
}
