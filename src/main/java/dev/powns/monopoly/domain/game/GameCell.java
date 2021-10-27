package dev.powns.monopoly.domain.game;

import dev.powns.monopoly.domain.estate.RealEstate;

public class GameCell {
	private GameCellType gameCellType;
	private RealEstate purchaseableEstate;

	private int minX;
	private int minZ;
	private int maxX;
	private int maxZ;

	public GameCell(GameCellType gameCellType, int minX, int minZ, int maxX, int maxZ) {
		this(gameCellType, null, minX, minZ, maxX, maxZ);
	}

	public GameCell(RealEstate purchaseableEstate, int minX, int minZ, int maxX, int maxZ) {
		this(GameCellType.BUYABLE, purchaseableEstate, minX, minZ, maxX, maxZ);
	}

	public GameCell(GameCellType gameCellType, RealEstate purchaseableEstate, int minX, int minZ, int maxX, int maxZ) {
		this.gameCellType = gameCellType;

		this.purchaseableEstate = purchaseableEstate;

		this.minX = minX;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxZ = maxZ;
	}

	public int getMinX() {
		return this.minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinZ() {
		return this.minZ;
	}

	public void setMinZ(int minZ) {
		this.minZ = minZ;
	}

	public int getMaxX() {
		return this.maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxZ() {
		return this.maxZ;
	}

	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
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
