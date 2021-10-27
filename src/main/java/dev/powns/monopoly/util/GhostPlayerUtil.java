package dev.powns.monopoly.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.powns.monopoly.MonopolyPlugin;
import dev.powns.monopoly.domain.player.Team;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EnumChatFormat;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.Scoreboard;
import net.minecraft.server.v1_15_R1.ScoreboardTeam;
import net.minecraft.server.v1_15_R1.ScoreboardTeamBase;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GhostPlayerUtil {
	private static final Map<UUID, Map.Entry<String, String>> cachedSkinData = new HashMap<>();
	private static final Map<UUID, Integer> playerCloneGhosts = new HashMap<>();

	public static void ghostTeam(Team team) {
		for (UUID playerId : team.getTeamMembers()) {
			Player player = Bukkit.getPlayer(playerId);

			if (player == null) {
				continue;
			}

			GhostPlayerUtil.ghostPlayer(player);
		}
	}

	public static void ghostPlayer(Player player) {
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld("world")).getHandle();

		if (GhostPlayerUtil.playerCloneGhosts.containsKey(player.getUniqueId())) {
			for (Player connectedPlayer : Bukkit.getOnlinePlayers()) {
				PlayerConnection connection = ((CraftPlayer)connectedPlayer).getHandle().playerConnection;
				connection.sendPacket(new PacketPlayOutEntityDestroy(GhostPlayerUtil.playerCloneGhosts.get(player.getUniqueId())));

				connectedPlayer.showPlayer(MonopolyPlugin.getInstance(), player);

				if (player.getGameMode() != GameMode.CREATIVE) {
					player.setAllowFlight(false);
					player.setFlying(false);
				}
			}

			GhostPlayerUtil.playerCloneGhosts.remove(player.getUniqueId());
		} else {
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());

			Map.Entry<String, String> textureData = GhostPlayerUtil.getSkinData(player.getUniqueId());
			gameProfile.getProperties().put("textures", new Property("textures", textureData.getKey(), textureData.getValue()));

			EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
			npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

			for (Player connectedPlayer : Bukkit.getOnlinePlayers()) {
				PlayerConnection connection = ((CraftPlayer)connectedPlayer).getHandle().playerConnection;
				connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
				connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
				connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));

				DataWatcher watcher = npc.getDataWatcher();
				watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 0xFF); //0x20 makes NPC invisible but still rendered
				PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(npc.getId(), watcher, true);
				connection.sendPacket(packetPlayOutEntityMetadata);

				new BukkitRunnable() {
					@Override
					public void run() {
						connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
					}
				}.runTaskLater(MonopolyPlugin.getInstance(), 10L);

//				try {
//					PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam();
//
////					Field f = PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
////					f.setAccessible(true);
////					Collection<String> teamPlayers = (Collection<String>) f.get(packetPlayOutScoreboardTeam);
////					teamPlayers.add(gameProfile.getName());
//
//					Field f2 = PacketPlayOutScoreboardTeam.class.getDeclaredField("e");
//					f2.setAccessible(true);
//					f2.set(packetPlayOutScoreboardTeam, ScoreboardTeamBase.EnumNameTagVisibility.NEVER.e);
//
//					connection.sendPacket(packetPlayOutScoreboardTeam);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}

				connectedPlayer.hidePlayer(MonopolyPlugin.getInstance(), player);

				if (player.getGameMode() != GameMode.CREATIVE) {
					player.setAllowFlight(true);
					player.setFlying(true);
				}
			}

			((CraftServer) Bukkit.getServer()).getServer().getPlayerList().players.remove(npc);

			GhostPlayerUtil.playerCloneGhosts.put(player.getUniqueId(), npc.getId());
		}
	}

	private static Map.Entry<String, String> getSkinData(UUID playerId) {
		if (GhostPlayerUtil.cachedSkinData.containsKey(playerId)) {
			return GhostPlayerUtil.cachedSkinData.get(playerId);
		}

		Map.Entry<String, String> result = null;

		try {
			//47ca987c423f4a428119fae4677f5b0c
			String jsonString = GhostPlayerUtil.getJsonStringFromGet("https://sessionserver.mojang.com/session/minecraft/profile/" + playerId.toString() + "?unsigned=false");

			JSONObject json = new JSONObject(jsonString);
			JSONArray properties = json.getJSONArray("properties");

			if (properties.length() > 0) {
				JSONObject skinProperties = properties.getJSONObject(0);

				String value = skinProperties.getString("value");
				String signature = skinProperties.getString("signature");

				result = new AbstractMap.SimpleEntry<>(value, signature);
				GhostPlayerUtil.cachedSkinData.put(playerId, result);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return result;
	}

	private static String getJsonStringFromGet(String rawUrl) {
		String json = "";

		try {
			URL url = new URL(rawUrl);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			http.disconnect();

			json = sb.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return json;
	}
}
