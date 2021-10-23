package dev.powns.monopoly.domain.estate;

import dev.powns.monopoly.domain.LandingReason;
import dev.powns.monopoly.domain.TeamColorEnum;

public abstract class RealEstate {
	protected String name;
	protected int price;

	protected int housesPlaced = 0;
	protected boolean hotel;

	protected boolean refunded;
	protected TeamColorEnum estateOwner;

	public RealEstate(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public abstract int getAmountToPay(LandingReason landingReason, int thrownDiceAmount);

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isRefunded() {
		return this.refunded;
	}

	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}

	public int getHousesPlaced() {
		return this.housesPlaced;
	}

	public void setHousesPlaced(int housesPlaced) {
		this.housesPlaced = housesPlaced;
	}

	public boolean isHotel() {
		return this.hotel;
	}

	public void setHotel(boolean hotel) {
		this.hotel = hotel;
	}

	public TeamColorEnum getEstateOwner() {
		return this.estateOwner;
	}

	public void setEstateOwner(TeamColorEnum estateOwner) {
		this.estateOwner = estateOwner;
	}
}
