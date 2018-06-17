package net.upd4ting.gameapi.gamehandler;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;

/**
 * Interface that is used for spectator handling
 * @author Upd4ting
 *
 */
public interface SpectatorHandler {
	
	/**
	 * Called when a player join a game
	 * @param g The game
	 * @param p The player that join the game
	 */
    void onJoin(Game g, Player p);
	
	/**
	 * Set the spectator mode on a player
	 * @param p The player to set in spectator mode
	 */
    void setSpectator(Game g, Player p);
}
