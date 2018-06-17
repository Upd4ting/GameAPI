package net.upd4ting.gameapi.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called
 * when a player die
 * @author Upd4ting
 *
 */
public class GameDieEvent extends Event {
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

	public GameDieEvent(Player player, Game game) {
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
