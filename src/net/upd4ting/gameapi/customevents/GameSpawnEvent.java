package net.upd4ting.gameapi.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;

/**
 * This custom listener will be called when a player spawn
 * @author Upd4ting
 *
 */
public final class GameSpawnEvent extends Event {
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
	private final boolean respawn;

	public GameSpawnEvent(Player player, Game game, boolean respawn) {
		this.player = player;
		this.game = game;
		this.respawn = respawn;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}
	
	public boolean isRespawn() {
		return respawn;
	}
}
