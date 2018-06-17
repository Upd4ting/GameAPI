package net.upd4ting.gameapi.board;

import java.util.HashMap;
import java.util.UUID;

import net.upd4ting.gameapi.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.gamehandler.BoardHandler;
import net.upd4ting.gameapi.task.TaskManager;

/**
 * Class that handle scoreboard
 * @author Upd4ting
 *
 */
public final class Board implements Runnable {
	
	private static final String TASK_NAME = "Board";
	private static Board instance;
	
	private static final HashMap<UUID, Sideline> playerSideline = new HashMap<>();
	
	private Board() {} // Non instanciable class
	
	public void start() {
		TaskManager.runTask(TASK_NAME, this, 20);
	}
	
	public void stop() {
		TaskManager.cancelTask(TASK_NAME);
	}
	
	@Override
	public void run() {
		for (Game game : GameManager.getGames()) {
			if (game.getBoardHandler() == null) continue;
			
			BoardHandler handler = game.getBoardHandler();
			
			for (Player p : game.getInsides()) {
				Sideline line = playerSideline.get(p.getUniqueId());
				
				line.sb.setName(PlaceHolder.execute(game, p, null, handler.getTitle()));
				
				handler.build(game, p, line.getFlusher());
				line.flush();
				
		        // -- Life display in scoreboard
				if (handler.getDisplayStyle() != null) {
					initDisplay(game, p, handler.getDisplayStyle());
				}
			}
		}
	}
	
	public void init(Game game, Player p) {
		if (game.getBoardHandler() == null)
			return;
		
		Sidebar sb = new Sidebar(p);
		BoardHandler handler = game.getBoardHandler();
		
		sb.setName(handler.getTitle());
				
        // -- Life display in scoreboard
		if (handler.getDisplayStyle() != null) {
			initDisplay(game, p, handler.getDisplayStyle());
		}
		
		playerSideline.put(p.getUniqueId(), new Sideline(sb));
	}
	
	public void leave(Player p) {
		Sideline sl = playerSideline.remove(p.getUniqueId());
		sl.sb.remove();
	}
	
	private void initDisplay(Game game, Player p, String displayStyle) {
        Scoreboard scoreboard = p.getScoreboard();
        Objective o = scoreboard.getObjective("health");
                
        if (o == null) {
        	o = scoreboard.registerNewObjective("health", displayStyle);
            o.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        
        for (Player a : game.getInsides())
            o.getScore(a.getName()).setScore((int) a.getHealth());
	}
	
	public static Board getInstance() {
		if (instance == null)
			instance = new Board();
		return instance;
	}
}
