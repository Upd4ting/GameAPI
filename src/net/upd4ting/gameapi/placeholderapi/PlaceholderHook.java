package net.upd4ting.gameapi.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.board.PlaceHolder;

public class PlaceholderHook extends EZPlaceholderHook {

	public PlaceholderHook(Plugin plugin) {
		super(plugin, plugin.getDescription().getName().toLowerCase());
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		Game game = GameManager.getPlayerGame(p);
		
		if (game == null)
			return null;
		
		return PlaceHolder.translateForPlaceholderAPI(game, identifier, p);
	}

}
