package net.upd4ting.gameapi.customevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called
 * when the game start
 * @author Upd4ting
 *
 */
public final class GameStartEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	 
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	private final Game game;

	public GameStartEvent(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}
