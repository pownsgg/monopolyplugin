package dev.powns.monopoly.generation;

import dev.powns.monopoly.domain.game.GameCell;
import dev.powns.monopoly.domain.game.GameCellType;
import dev.powns.monopoly.domain.estate.RealEstate;
import dev.powns.monopoly.domain.estate.RegularEstate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class BoardGenerator {
	public void generateBoard(List<GameCell> gameCells) {
		World world = Bukkit.getServer().getWorld("world");

		for (int x = -39; x <= 46; x++) {
			for (int y = -46; y <= 39; y++) {
				Location loc = new Location(world, x, 110, y);

				loc.getBlock().setType(Material.BLACK_CONCRETE);
			}
		}

		for (int x = -27; x <= 34; x++) {
			for (int y = -34; y <= 27; y++) {
				Location loc = new Location(world, x, 110, y);

				loc.getBlock().setType(Material.QUARTZ_PILLAR);
			}
		}

		for (Entity entity : world.getEntities()) {
			if (entity instanceof ArmorStand) {
				entity.remove();
			}
		}

		for (GameCell gameCell : gameCells) {
			Material blockMaterial = Material.QUARTZ_PILLAR;

			if (gameCell.getPurchaseableEstate() != null) {
				RealEstate realEstate = gameCell.getPurchaseableEstate();

				if (realEstate instanceof RegularEstate) {
					blockMaterial = ((RegularEstate) realEstate).getRegularEstateColor().getBlockMaterial();
				}
			}

			for (int x = gameCell.getMinX(); x <= gameCell.getMaxX(); x++) {
				for (int y = gameCell.getMinZ(); y <= gameCell.getMaxZ(); y++) {
					Location loc = new Location(world, x, 110, y);

					loc.getBlock().setType(blockMaterial);
				}
			}

			int signX = (gameCell.getMinX() + gameCell.getMaxX()) / 2;
			int signY = (gameCell.getMinZ() + gameCell.getMaxZ()) / 2;

			Location location = new Location(world, signX, 111, signY);
			Location location2 = new Location(world, signX, 110.75, signY);

			ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
			armorStand.setGravity(false);
			armorStand.setVisible(false);
			armorStand.setCustomNameVisible(true);
			armorStand.setCustomName(gameCell.getGameCellType() == GameCellType.BUYABLE ? gameCell.getPurchaseableEstate().getName() : gameCell.getGameCellType().name());

			if (gameCell.getGameCellType() == GameCellType.BUYABLE) {
				ArmorStand armorStand2 = (ArmorStand) world.spawnEntity(location2, EntityType.ARMOR_STAND);
				armorStand2.setGravity(false);
				armorStand2.setVisible(false);
				armorStand2.setCustomNameVisible(true);
				armorStand2.setCustomName("" + gameCell.getPurchaseableEstate().getPrice());
			}
		}
	}
}
