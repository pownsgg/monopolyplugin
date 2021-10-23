package dev.powns.monopoly.domain.estate;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.LandingReason;

public class RegularEstate extends RealEstate {
	private final RegularEstateColor regularEstateColor;
	private int[] amountToPay;

	public RegularEstate(RegularEstateColor regularEstateColor, String name, int price, int[] amountToPay) {
		super(name, price);

		this.regularEstateColor = regularEstateColor;
		this.amountToPay = amountToPay;
	}

	public RegularEstateColor getRegularEstateColor() {
		return this.regularEstateColor;
	}

	@Override
	public int getAmountToPay(LandingReason landingReason, int thrownDiceAmount) {
		boolean fullStreetOwned = MonopolyPlugin.getInstance().getGameManager().getCurrentGame().fullStreetOwned(estateOwner, this.regularEstateColor);

		if (this.hotel) {
			return this.amountToPay[5];
		} else if (this.housesPlaced > 0) {
			return this.amountToPay[this.housesPlaced];
		} else if (fullStreetOwned) {
			return this.amountToPay[0] * 2;
		} else {
			return this.amountToPay[0];
		}
	}

	@Override
	public String getName() {
		return this.regularEstateColor.nameColor + super.name;
	}
}
