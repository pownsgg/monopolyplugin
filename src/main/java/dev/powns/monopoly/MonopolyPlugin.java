package dev.powns.monopoly;

import dev.powns.monopoly.commands.TestCommand;
import dev.powns.monopoly.generation.BoardGenerator;
import dev.powns.monopoly.generation.LobbyGenerator;
import dev.powns.monopoly.listeners.GameListener;
import dev.powns.monopoly.listeners.GeneralListener;
import dev.powns.monopoly.managers.GameManager;
import dev.powns.monopoly.managers.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MonopolyPlugin extends JavaPlugin {
	private static MonopolyPlugin instance;

	private final GameManager gameManager = new GameManager();
	private final MenuManager menuManager = new MenuManager();

	private final LobbyGenerator lobbyGenerator = new LobbyGenerator();
	private final BoardGenerator boardGenerator = new BoardGenerator();

	public void onEnable() {
		MonopolyPlugin.instance = this;

		try {
			this.lobbyGenerator.generateLobby();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this.menuManager, this);
		pm.registerEvents(new GeneralListener(), this);
		pm.registerEvents(new GameListener(), this); // TODO: only register if game started.

		this.getCommand("monopoly").setExecutor(new TestCommand());
	}

	public static MonopolyPlugin getInstance() {
		return MonopolyPlugin.instance;
	}

	public GameManager getGameManager() {
		return this.gameManager;
	}

	public MenuManager getMenuManager() {
		return this.menuManager;
	}

	public LobbyGenerator getLobbyGenerator() {
		return this.lobbyGenerator;
	}

	public BoardGenerator getBoardGenerator() {
		return this.boardGenerator;
	}
}
