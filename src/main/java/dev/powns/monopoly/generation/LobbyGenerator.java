package dev.powns.monopoly.generation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.powns.monopoly.MonopolyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;

public class LobbyGenerator {
	public void generateLobby() {
		World world = Bukkit.getServer().getWorld("world");

		try {
			File lobbyFile = new File(MonopolyPlugin.getInstance().getDataFolder().getAbsolutePath() + "/lobby.schem");
			ClipboardFormat format = ClipboardFormats.findByFile(lobbyFile);

			ClipboardReader reader = format.getReader(new FileInputStream(lobbyFile));
			Clipboard clipboard = reader.read();

			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
			Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(0, 151, 0)).ignoreAirBlocks(true).build();

			try {
				Operations.complete(operation);
				editSession.flushSession();
			} catch (WorldEditException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			for (int i = -9; i < 10; i++) {
				for (int j = 150; j < 155; j++) {
					Location loc = new Location(world, i, j, -9);
					Location loc2 = new Location(world, i, j, 9);
					Location loc3 = new Location(world, 9, j, i);
					Location loc4 = new Location(world, -9, j, i);

					loc.getBlock().setType(Material.IRON_BARS);
					loc2.getBlock().setType(Material.IRON_BARS);
					loc3.getBlock().setType(Material.IRON_BARS);
					loc4.getBlock().setType(Material.IRON_BARS);
				}
			}

			for (int i = -9; i < 10; i++) {
				for (int j = -9; j < 10; j++) {
					Location loc = new Location(world, i, 150, j);
					Location loc2 = new Location(world, i, 155, j);

					loc.getBlock().setType(Material.IRON_BLOCK);
					loc2.getBlock().setType(Material.LIGHT_GRAY_STAINED_GLASS);
				}
			}
		}
	}
}
