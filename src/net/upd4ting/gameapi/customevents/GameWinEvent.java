package net.upd4ting.gameapi.customevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.team.Team;

/**
 * This custom listener will be called when
 * a team win
 * @author Upd4ting
 *
 */
public class GameWinEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	 
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	private final Game game;
	private Team team;

	public GameWinEvent(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
	
	public Team getTeam() {
		return team;
	}
}
