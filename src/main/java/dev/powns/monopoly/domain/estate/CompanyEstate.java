package dev.powns.monopoly.domain.estate;

import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.LandingReason;
import dev.powns.monopoly.managers.GameManager;

public class CompanyEstate extends RealEstate {
	public CompanyEstate(String name, int price) {
		super(name, price);
	}

	@Override
	public int getAmountToPay(LandingReason landingReason, int thrownDiceAmount) {
		GameManager gameManager = MonopolyPlugin.getInstance().getGameManager();
		int companiesOwnedByOwner = 0;

		for (RealEstate realEstate : gameManager.getCurrentGame().getEstateOwned().get(this.estateOwner)) {
			if (realEstate instanceof CompanyEstate) {
				companiesOwnedByOwner++;
			}
		}

		return thrownDiceAmount * (companiesOwnedByOwner == 2 ? 10 : 4);
	}
}
