package net.upd4ting.gameapi.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.schematic.Schematic.BlockInfo;

/**
 * Class to handle team in our games!
 * @author Upd4ting
 *
 */
public class Team {
	
	private static int _id = 0;
	
	private final Game game;
	private final Integer id;
	private String name;
	private String prefix;
	private BlockInfo block;
	private Player owner;
	private final List<Player> players;
	private org.bukkit.scoreboard.Team handler;

	// Custom data
	private Map<String, Object> datas = new HashMap<>();
	
	/**
	 * Default constructor
	 * Called each time
	 */
	public Team(Game game) {
		this.players = new ArrayList<>();
		this.game = game;
		this.id = _id++;
	}
	
	/**
	 * Constructor for no GUI system
	 * @param owner Owner of the team
	 */
	public Team(Game game, Player owner) {
		this(game);
		this.owner = owner;
	}
	
	/**
	 * Constructor for gui system
	 * @param name Name of the team
	 * @param block Block to display in the gui
	 * @param prefix Prefix of the team
	 */
	public Team(Game game, String name, BlockInfo block, String prefix) {
		this(game);
		this.name = name;
		this.block = block;
		this.prefix = prefix;
	}
	
	/**
	 * Method to call to make a player join the team
	 * @param p The player
	 */
	public void join(Player p) {
		players.add(p);

		if (handler == null) {
			handler = getHandlerTeam(game.getName() + id);
			if (prefix != null) {
				handler.setPrefix(prefix + " ");
				handler.setSuffix(ChatColor.RESET + "");
			} else {
				handler.setPrefix("");
				handler.setSuffix("");
			}
		}
		handler.addPlayer(p);
	}
	
	/**
	 * Method to call to make a player leave the team
	 * @param p The player
	 */
	public void leave(Player p) {
		players.remove(p);

		if (handler == null) {
			handler = getHandlerTeam(game.getName() + id);
			if (prefix != null) {
				handler.setPrefix(prefix + " ");
				handler.setSuffix(ChatColor.RESET + "");
			} else {
				handler.setPrefix("");
				handler.setSuffix("");
			}
		}

		handler.removePlayer(p);
	}
	
	/**
	 * Get number of player that are alive!
	 * @return The value
	 */
	public Integer getPlayerAlive() {
		Integer n = 0;
		for (Player p : getPlayers()) {
			if (game.getPlaying().contains(p))
				n++;
		}
		return n;
	}

	private org.bukkit.scoreboard.Team getHandlerTeam(String name) {
		try {
			org.bukkit.scoreboard.Team handler = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
			return handler;
		} catch (Exception e) {
			org.bukkit.scoreboard.Team handler = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
			return handler;
		}
 	}
	
	/**
	 * @return If the team is alive or not
	 */
	public boolean isAlive() {
		return getPlayerAlive() <= 0;
	}
	
	/**
	 * @return The game where the team is
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * @return The name of the team
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The prefix of the team
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * @return The icon of the team
	 */
	public BlockInfo getBlock() {
		return block;
	}

	/**
	 * @return The owner of the team
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * @param owner The new owner of the team
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	/**
	 * @return A copy of the players that are in the team
	 */
	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}
	
	/**
	 * @return The handler of the team
	 */
	public org.bukkit.scoreboard.Team getHandler() {
		return handler;
	}

	public <T> void setData(String name, T value) {
		this.datas.put(name, value);
	}

	public <T> T getData(String name) {
		return (T) this.datas.get(name);
	}
	
}
