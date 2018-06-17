package net.upd4ting.gameapi.customevents;

import net.upd4ting.gameapi.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This custom listener will be called when a player
 * join a game
 * @author Upd4ting
 *
 */
public final class GameRejoinEvent extends Event implements Cancellable {
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
	private boolean cancelled;

	public GameRejoinEvent(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
