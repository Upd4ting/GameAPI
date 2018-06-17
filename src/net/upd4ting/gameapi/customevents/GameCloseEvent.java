package net.upd4ting.gameapi.customevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called
 * when a game is closed
 * @author Upd4ting
 *
 */
public class GameCloseEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	 
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	private final Game game;

	public GameCloseEvent(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}
