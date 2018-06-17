package net.upd4ting.gameapi.gamehandler;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.board.LineFlusher;

/**
 * Class to handle the scoreboard in the game
 * @author Upd4ting
 *
 */
public interface BoardHandler {
	
	/**
	 * Get the title of the scoreboard
	 * @return The title
	 */
    String getTitle();
	
	/**
	 * Get the display style
	 * @return @null if nothing, otherwise 'health' or 'dummy'
	 */
    String getDisplayStyle();
	
	/**
	 * Method that will be called to build the scoreboard
	 * @param game The game
	 * @param p The player
	 * @param flusher The line flusher
	 */
    void build(Game game, Player p, LineFlusher flusher);
}
