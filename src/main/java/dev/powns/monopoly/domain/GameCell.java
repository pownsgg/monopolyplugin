package dev.powns.monopoly.domain;

import dev.powns.monopoly.domain.estate.RealEstate;

public class GameCell {
	private GameCellType gameCellType;
	private RealEstate purchaseableEstate;

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	public GameCell(GameCellType gameCellType, int minX, int minY, int maxX, int maxY) {
		this(gameCellType, null, minX, minY, maxX, maxY);
	}

	public GameCell(RealEstate purchaseableEstate, int minX, int minY, int maxX, int maxY) {
		this(GameCellType.BUYABLE, purchaseableEstate, minX, minY, maxX, maxY);
	}

	public GameCell(GameCellType gameCellType, RealEstate purchaseableEstate, int minX, int minY, int maxX, int maxY) {
		this.gameCellType = gameCellType;

		this.purchaseableEstate = purchaseableEstate;

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public int getMinX() {
		return this.minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return this.minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return this.maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return this.maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public GameCellType getGameCellType() {
		return this.gameCellType;
	}

	public void setGameCellType(GameCellType gameCellType) {
		this.gameCellType = gameCellType;
	}

	public RealEstate getPurchaseableEstate() {
		return this.purchaseableEstate;
	}

	public void setPurchaseableEstate(RealEstate purchaseableEstate) {
		this.purchaseableEstate = purchaseableEstate;
	}
}
