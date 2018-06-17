package net.upd4ting.gameapi;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * Interface to handle sub game command
 * @author Upd4ting
 *
 */
public interface SubcommandHandler {
	
	/**
	 * Handle some more game command
	 * @param p Player that executed the game command
	 * @param args Arguments of the command
	 * @return true if the command has been handled or false if not
	 */
	boolean handleSubCommand(Player p, String args[]);
	
	/**
	 * Get help of subcommand
	 * @return The help of additionnal command
	 */
	List<String> getAdditionnalHelpCommand();
}
