package net.upd4ting.gameapi.board;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameState;
import net.upd4ting.gameapi.board.placeholders.GameTime;
import net.upd4ting.gameapi.board.placeholders.Kill;
import net.upd4ting.gameapi.board.placeholders.KillGame;
import net.upd4ting.gameapi.board.placeholders.PlayerAlive;
import net.upd4ting.gameapi.board.placeholders.PlayerMaximum;
import net.upd4ting.gameapi.board.placeholders.PlayerSpectator;
import net.upd4ting.gameapi.board.placeholders.PlayerTotal;
import net.upd4ting.gameapi.board.placeholders.TeamAlive;
import net.upd4ting.gameapi.board.placeholders.YLayer;

/**
 * Class to handle placeholder in the scoreboard
 * @author Upd4ting
 *
 */
public abstract class PlaceHolder {
	public static final HashSet<PlaceHolder> placeHolders = new HashSet<>();
	
	private final List<GameState> state;
	private final String name;
	
	public PlaceHolder(String name, GameState... states) {
		this.name = name;
		this.state = Arrays.asList(states);
	}
	
	public abstract String process(Game game, Player p, Sideline sl, String conserned);
	
	public String getName() { 
		return name; 
	}
	
	public List<GameState> getStates() { 
		return state; 
	}
	
	public static String execute(Game game, Player p, Sideline sl, String line) {
		for (PlaceHolder v : placeHolders) {
			if (line.contains(v.getName()) && v.getStates().contains(game.getState())) {
				String result = v.process(game, p, sl, line);
				if (result == null && sl != null) {
					line = result;
					break; // Line has been treated
				} else if (result != null)
					line = result;
			}
		}
		
		// PlaceHolderAPI
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && line != null) {
			line = PlaceholderAPI.setPlaceholders(p, line);
		}
		
		if (line != null && sl != null)
			sl.add(line);
		
		return line;
	}
	
	public static String translateForPlaceholderAPI(Game game, String identifier, Player p) {
		String copy = "%" + identifier;
		for (PlaceHolder v : placeHolders) {
			if (copy.contains(v.getName())) {
				String s = v.process(game, p, null, copy);
				
				if (s != null)
					return s;
			}
		}
		return null;	
	}
	
	public static void init() {
		registerPlaceholder(new Kill());
		registerPlaceholder(new PlayerAlive());
		registerPlaceholder(new PlayerMaximum());
		registerPlaceholder(new PlayerSpectator());
		registerPlaceholder(new PlayerTotal());
		registerPlaceholder(new TeamAlive());
		registerPlaceholder(new YLayer());
		registerPlaceholder(new KillGame());
		registerPlaceholder(new GameTime());
	}
	
	public static void registerPlaceholder(PlaceHolder placeholder) {
		placeHolders.add(placeholder);
	}
}
