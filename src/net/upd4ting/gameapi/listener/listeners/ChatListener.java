package net.upd4ting.gameapi.listener.listeners;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.configuration.configs.LangConfig;
import net.upd4ting.gameapi.team.Team;

public class ChatListener implements Listener {
	
	enum RecipientType { TEAM, SPEC, ALL }
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Game game = GameManager.getPlayerGame(p);
		
		if (game == null)
			return;
		
		LangConfig handler = GameAPI.getLangConfiguration();
		
		Team team = game.getTeam(p);
		
		String prefix = "";
		String message = e.getMessage();
		
		// Set du prefix
		if (game.isInGame()) {
			// Spectateur
			if (game.getSpectators().contains(p)) {
				prefix = handler.getChatPrefixSpectator();
				setRecipients(game, e, RecipientType.SPEC);
			}
			else {
				if (!game.isFFA()) {
					if (team == null || team.getPlayers().size() == 1 || message.startsWith(handler.getChatPrefixGlobalMessage())) {
						if (message.startsWith(handler.getChatPrefixGlobalMessage()))
							message = message.replaceFirst(handler.getChatPrefixGlobalMessage(), "");
						prefix = handler.getChatPrefixAll();
						setRecipients(game, e, RecipientType.ALL);
					} else {
						prefix = handler.getChatPrefixTeam();
						setRecipients(game, e, RecipientType.TEAM);
					}
				} else {
					prefix = handler.getChatPrefixAll();
					setRecipients(game, e, RecipientType.ALL);
				}
			}
		}
		
		if (!game.getChatFormatting())
			return;
		
		String format = handler.getChatFormat().replace("%message%", "%s")
				.replace("%name%", "%s").replace("%prefixteam%", team == null || team.getPrefix() == null ? "" : team.getPrefix())
				.replace("%prefix%", prefix);
		e.setMessage(message);
		e.setFormat(format);
	}
	
	private void setRecipients(Game game, AsyncPlayerChatEvent e, RecipientType type) {
		if (type == RecipientType.SPEC) {
			for (Player current : new ArrayList<>(e.getRecipients()))
				if (!game.getSpectators().contains(current))
					e.getRecipients().remove(current);
		} else if (type == RecipientType.TEAM) {
			Team t = game.getTeam(e.getPlayer());
			
			for (Player current : new ArrayList<>(e.getRecipients()))
				if (!t.getPlayers().contains(current))
					e.getRecipients().remove(current);
		}
		
		for (Player current : new ArrayList<>(e.getRecipients())) {
			if (!game.getInsides().contains(current))
				e.getRecipients().remove(current);
		}
	}
}
