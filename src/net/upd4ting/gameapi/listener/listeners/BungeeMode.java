package net.upd4ting.gameapi.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.configuration.configs.LangConfig;


/**
 * Class that listen to @ServerListPingEvent
 * 
 * It will set the motd if bungee mode is enabled
 * 
 * @author Upd4ting
 *
 */
public class BungeeMode implements Listener {
	
	@EventHandler
	public void onServerList(ServerListPingEvent e) {
		if (GameAPI.getGameConfiguration().isBungeeMode()) {
			Game game = GameManager.getBungeeGame();
			LangConfig config = GameAPI.getLangConfiguration();
			
			if (game.isWaiting())
				e.setMotd(config.getMotdWaiting());
			else if (game.isInGame())
				e.setMotd(config.getMotdIngame());
			else
				e.setMotd(config.getMotdFinish());
		}
	}
}
