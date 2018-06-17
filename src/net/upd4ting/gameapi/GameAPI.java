package net.upd4ting.gameapi;

import net.upd4ting.gameapi.achook.CheatProtectionHandler;
import net.upd4ting.gameapi.configuration.configs.ConfLangConfig;
import net.upd4ting.gameapi.inventory.inv.config.*;
import net.upd4ting.gameapi.nms.common.*;
import net.upd4ting.gameapi.signs.SignTask;
import net.upd4ting.gameapi.specialitems.SpecialItem;
import net.upd4ting.gameapi.specialitems.items.BackItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.md_5.bungee.api.ChatColor;
import net.upd4ting.gameapi.board.Board;
import net.upd4ting.gameapi.board.PlaceHolder;
import net.upd4ting.gameapi.command.CommandManager;
import net.upd4ting.gameapi.configuration.Configuration;
import net.upd4ting.gameapi.configuration.Configuration.ConfType;
import net.upd4ting.gameapi.configuration.configs.GameConfig;
import net.upd4ting.gameapi.configuration.configs.LangConfig;
import net.upd4ting.gameapi.inventory.Inventory;
import net.upd4ting.gameapi.listener.ListenerManager;
import net.upd4ting.gameapi.nms.NMSHandler;
import net.upd4ting.gameapi.placeholderapi.PlaceholderHook;
import net.upd4ting.gameapi.signs.Sign;
import net.upd4ting.gameapi.stats.StatTask;
import net.upd4ting.gameapi.task.TaskManager;
import net.upd4ting.gameapi.util.Util;

/**
 * This class is the main class of the GameAPI.
 * 
 * @author Upd4ting
 *
 */
public class GameAPI {
	
	// Sert au register automatique de la seule game existante quand Bungee mode :)
	public interface GamePlugin { Game createGame(World world, String name); }
	
	static JavaPlugin instance;
	private static ProtocolManager pManager;
	private static GameConfig gameConfiguration;
	private static LangConfig langConfiguration;

	private static int hubItemId;
			
	// Things for AntiPiracy and update checker
	public static final String uid = "%%__USER__%%";
	public static final String RESOURCE = "%%__RESOURCE__%%";
	public static final String uidspigotlink = "https://spigotmc.org/members/" + uid;
	public static final String spigotmclink = "https://www.spigotmc.org/conversations/add?to=Upd4ting";
	
	/**
	 * Must be called at STARTUP
	 * @param plugin The javaplugin
	 */
	public static void init(JavaPlugin plugin) {
		
		// Init instance
		instance = plugin;

		if (!(plugin instanceof GamePlugin)) {
			Util.log(ChatColor.RED + "Unable to init GameAPI, main class must implements GamePlugin");
			Bukkit.shutdown();
			return;
		}
		
		// -- Init PM
		pManager = ProtocolLibrary.getProtocolManager();
		
		// Init NMSHandler & add basic module nms
		NMSHandler.init();
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "NametagEdit", NametagHandler.class);
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "Respawn", RespawnHandler.class);
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "SpecificWriter", SpecificWriterHandler.class);
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "TitleUtils", TitleHandler.class);
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "Paster", PasteHandler.class);
		NMSHandler.registerModule("net.upd4ting.gameapi.nms", "ActionBar", ActionBarHandler.class);

		// Register Inventory type
		InventoryType.registerType(Location.class, InvLocation::new);
		InventoryType.registerType(Boolean.class, InvBoolean::new);
		InventoryType.registerType(Integer.class, InvInt::new);
		InventoryType.registerType(Double.class, InvDouble::new);
		InventoryType.registerType(boolean.class, InvBoolean::new);
		InventoryType.registerType(int.class, InvInt::new);
		InventoryType.registerType(double.class, InvDouble::new);
		
		// Register commands
		CommandManager.registerAllCommand();
		
		// PlaceholderAPI
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PlaceholderHook(plugin).hook();
		}
		
		// Init TaskManager
		new TaskManager();
		
		// Init inventory
		Inventory.initialize();
		
		// Init sign system
		Sign.init();
		
		// Init placeholder system
		PlaceHolder.init();
		
		// Start board task
		Board.getInstance().start();
		
		// Start stat task
		StatTask.getInstance().start();
	}
	
	/**
	 * Must be called at startup but after the plugin have finished to register his startup config
	 */
	public static void initConfig() {
		gameConfiguration = Configuration.registerConfig(new GameConfig());
		langConfiguration = Configuration.registerConfig(new LangConfig());

		Configuration.registerConfig(new ConfLangConfig());
		
		Configuration.loadConfigs(ConfType.STARTUP);

		// Load world of bungee
		if (gameConfiguration.isBungeeMode())
			new GameWorldSnapshot().restore("world", "bungee");
	}
	
	/**
	 * Must be called at onEnabled
	 */
	public static void enable() {
		AntiLeak.checkBlacklist();
		
		Configuration.loadConfigs(ConfType.ENABLE);

		// Register special items
		hubItemId = SpecialItem.registerItem(new BackItem());

		// Load AC hooks
		CheatProtectionHandler.setup();

		// Register BungeeGame
		if (gameConfiguration.isBungeeMode()) {
			GamePlugin gp = (GamePlugin) instance;
			GameManager.registerGame(gp.createGame(Bukkit.getWorlds().get(0), "bungee"));
		} else {
			GameSaver.loadSavedGames();
			Sign.loadAll();
		}

		// Register listener
		ListenerManager.register(instance);
	}
	
	/**
	 * Must be called at onDisable
	 */
	public static void disable() {
		// Close all games
		for (Game g : GameManager.getGames())
			g.close(false);

		// Stop board task
		Board.getInstance().stop();
		
		// Stop stat task
		StatTask.getInstance().stop();

		// Save all the game
		GameSaver.saveAll();

		// Save all the signs
		Sign.saveAll();

		// Stop sign task
		SignTask.getInstance().stop();
	}
	
	/**
	 * Get the instance of GameAPI
	 * @return The instance of the GameAPI
	 */
	public static JavaPlugin getInstance() {
		return instance;
	}
	
	/**
	 * @return Protocol Manager
	 */
	public static ProtocolManager getProtocolManager() { 
		return pManager; 
	}
	
	/**
	 * @return The game configuration
	 */
	public static GameConfig getGameConfiguration() {
		return gameConfiguration;
	}
	
	/**
	 * @return The lang configuration
	 */
	public static LangConfig getLangConfiguration() {
		return langConfiguration;
	}

	public static int getHubItemId() {
		return hubItemId;
	}
}
