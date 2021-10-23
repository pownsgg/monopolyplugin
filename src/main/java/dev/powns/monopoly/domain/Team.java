package dev.powns.monopoly.domain;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
	private final TeamColorEnum teamColor;
	private final ArrayList<UUID> teamMembers;

	private boolean jailed;
	private int money = 0;

	public Team(TeamColorEnum teamColor) {
		this.teamColor = teamColor;
		this.teamMembers = new ArrayList<>();
	}

	public boolean addPlayer(UUID playerId) {
//		if (this.teamMembers.size() < BingoPlugin.getInstance().getSettingsManager().getPluginConfiguration().getTeamSize().getConfigValue()) {
//			this.teamMembers.add(playerId);
//			return true;
//		}
//
//		return false;

		this.teamMembers.add(playerId);
		return true;
	}

	public void removePlayer(UUID playerId) {
		this.teamMembers.remove(playerId);
	}

	public ArrayList<UUID> getTeamMembers() {
		return this.teamMembers;
	}

	public TeamColorEnum getTeamColor() {
		return this.teamColor;
	}

	public boolean isJailed() {
		return this.jailed;
	}

	public void setJailed(boolean jailed) {
		this.jailed = jailed;
	}

	public int getMoney() {
		return this.money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
