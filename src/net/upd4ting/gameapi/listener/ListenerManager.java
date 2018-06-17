package net.upd4ting.gameapi.listener;

import net.upd4ting.gameapi.listener.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.upd4ting.gameapi.inventory.InventoryListener;

/**
 * This class will register all the listener of the GameAPI.
 * 
 * @author Upd4ting
 *
 */
public class ListenerManager {
	
	public static void register(JavaPlugin plugin) {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new JoinLeave(), plugin);
		pm.registerEvents(new BungeeMode(), plugin);
		pm.registerEvents(new Block(), plugin);
		pm.registerEvents(new InventoryListener(), plugin);
		pm.registerEvents(new Block(), plugin);
		pm.registerEvents(new ChatListener(), plugin);
		pm.registerEvents(new SpectatorListener(), plugin);
		pm.registerEvents(new DamageListener(), plugin);
	}
	
}
