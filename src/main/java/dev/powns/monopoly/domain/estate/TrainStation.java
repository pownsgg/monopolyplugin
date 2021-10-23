package dev.powns.monopoly.domain.estate;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.LandingReason;
import dev.powns.monopoly.managers.GameManager;

public class TrainStation extends RealEstate {
	public TrainStation(String name, int price) {
		super(name, price);
	}

	@Override
	public int getAmountToPay(LandingReason landingReason, int thrownDiceAmount) {
		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
		int amountToPay = 0;

		for (RealEstate realEstate : gameManager.getCurrentGame().getEstateOwned().get(this.estateOwner)) {
			if (realEstate instanceof TrainStation) {
				amountToPay = amountToPay == 0 ? 25 : amountToPay * 2;
			}
		}

		return landingReason == LandingReason.CARD ? amountToPay * 2 : amountToPay;
	}
}
