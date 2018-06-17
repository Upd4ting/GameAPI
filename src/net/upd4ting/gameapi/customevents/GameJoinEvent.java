package net.upd4ting.gameapi.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called when a player
 * join a game
 * @author Upd4ting
 *
 */
public final class GameJoinEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	 
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	private final Player player;
	private final Game game;

	public GameJoinEvent(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}

}
