package net.upd4ting.gameapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * This class manage all the game
 * It handles:
 *  - Game registering
 *  - Get all game availaible
 *  - Get info on a game (for sign)
 * 
 * @author Upd4ting
 *
 */
public class GameManager {
	
	// Cette liste a besoin d'être synchronisée
	private static final List<Game> games = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * This method register a game.
	 * 
	 * @param game The game to register
	 */
	public static void registerGame(Game game) {
		games.add(game);
		GameSaver.save(game);
	}

	/**
	 * This method unregister a game.
	 *
	 * @param game The game to unregister
	 */
	public static void unregisterGame(Game game) {
		games.remove(game);
	}
	
	/**
	 * Get a game with a name
	 * @param name THe name of the game that we want
	 * @return The game which have this gameId or @null if 
	 *         no exist
	 */
	public static Game getGame(String name) {
		for (Game game : games)
			if (game.getName().equals(name))
				return game;
		return null;
	}

	/**
	 * Get a game with a world name
	 * @param world The world name
	 * @return The game or @null if no exist
	 */
	public static Game getGameByWorld(String world) {
		for (Game game : games)
			if (game.getWorld().getName().equals(world))
				return game;
		return null;
	}
	
	/**
	 * Get player game
	 * @param p The player to get the game
	 * @return The game or @null if not in game
	 */
	public static Game getPlayerGame(Player p) {
		for (Game game : games)
			if (game.getInsides().contains(p))
				return game;
		return null;
	}
	
	/**
	 * Get if a player is limited or not
	 * @param p The player
	 * @return true/false
	 */
	public static boolean isLimited(Player p) {
		Game g = getPlayerGame(p);
		return g != null && (g.isWaiting() || g.getSpectators().contains(p) || g.isFinished());
	}
	
	/**
	 * Know if an entity can spawn or not
	 * @param ent  The entity
	 * @return true/false
	 */
	public static boolean canSpawn(LivingEntity ent) {
		for (Game game : games) {
			if (game.getWorld().equals(ent.getWorld()) && (game.isWaiting() || game.isFinished()))
				return false;
		}
		return true;
	}
	
	/**
	 * Get the game that is used for the bungee mode!
	 * @return Game for bungee mode
	 */
	public static Game getBungeeGame() {
		return games.size() > 0 ? games.get(0) : null;
	}
	
	/**
	 * Return the registred games.
	 */
	public static List<Game> getGames() {
		return games;
	}
}
