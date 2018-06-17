package net.upd4ting.gameapi.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.GamePlayer;
import net.upd4ting.gameapi.databaselib.DatabaseConnection;
import net.upd4ting.gameapi.util.Util;

/**
 * Class that handle player statistiques!
 * @author Upd4ting
 *
 */
public enum 	Stat {
	DEATH("death", true),
	KILL("globalKill", true),
	KILLGAME("gamekill", false),
	WIN("win", true),
	GAMELOSE("gamelose", true),
	GAMEPLAYED("gameplayed", true);
	
	private final String id;
	private final boolean saved;
	
	Stat(String id, boolean saved) {
		this.id = id;
		this.saved = saved;
	}
	
	public int get(Player p) {
		GamePlayer gp = GamePlayer.instanceOf(p);

		return gp.getStats().getOrDefault(this, 0);
	}

	public String getId() {
		return id;
	}

	public boolean isSaved() {
		return saved;
	}
	
	public static Stat getByID(String id) {
		for (Stat s : Stat.values())
			if (s.getId().equals(id))
				return s;
		return null;
	}
	
	public static void loadTable(Game game) {
		if (!game.isSqlEnabled())
			return;
		
	    StringBuilder sqlCreate = new StringBuilder("CREATE TABLE IF NOT EXISTS " + game.getStatTable() + " " +
				"(uuid VARCHAR(150) NOT NULL");
	    
	    for (Stat stat : Stat.values()) {
	    	sqlCreate.append(",").append(stat.getId()).append(" INT UNSIGNED NOT NULL DEFAULT '0'");
	    }
	    
	    sqlCreate.append(",PRIMARY KEY (uuid));");
	    
	    DatabaseConnection con = game.getPool().getConnection();
	    con.executeUpdate(sqlCreate.toString(), new ArrayList<>());
	    game.getPool().returnConnection(con);
	}

	public static void increase(Player p, Stat stat, int value) {
		GamePlayer gp = GamePlayer.instanceOf(p);
		Map<Stat, Integer> stats = gp.getStats();
		int current = stats.getOrDefault(stat, 0);
		stats.put(stat, current+value);
		gp.setStats(stats);
	}
	
	public static void load(Game game, Player p) {
		if (!game.isSqlEnabled())
			return;
		
		GamePlayer gp = GamePlayer.instanceOf(p);
		Map<Stat, Integer> stats = gp.getStats();
		
		String table = game.getStatTable();
		
		
		DatabaseConnection con = game.getPool().getConnection();
		List<Map<String, Object>> result = con.executeQuery("SELECT * FROM "+ table + " WHERE uuid = '" + p.getUniqueId().toString() + "'", new ArrayList<>());
		
		if (result == null || result.size() > 1) {
			Util.log(ChatColor.RED + "Problem from stat system. Contact upd4ting");
			game.getPool().returnConnection(con);
			return;
		}
		
		Map<String, Object> line = result.get(0);
		
		for (String id : line.keySet()) {
			if (id.equals("uuid"))
				continue;
			
			Stat stat = Stat.getByID(id);
			stats.put(stat, (Integer) line.get(id));
		}
		
		game.getPool().returnConnection(con);
	}
	
	public static void save() {
		
		for (Game game : GameManager.getGames()) {
			if (!game.isInGame() || !game.isSqlEnabled()) continue;
			
			save(game, false);
		}
	}
	
	public static void save(Game game, boolean last) {
		if (!game.isSqlEnabled())
			return;
		
		for (Player p : game.getInsides())
			save(game, p, last);
	}
	
	private static void save(Game game, Player p, boolean last) {
		GamePlayer gp = GamePlayer.instanceOf(p);
		Map<Stat, Integer> stats = gp.getStats();
		
		String table = game.getStatTable();
		
		StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (`uuid`");
		
		for (Stat s : Stat.values()) {
			if (!s.isSaved()) 
				continue;
			
			sql.append(",`").append(s.getId()).append("`");
		}
		
		sql.append(") VALUES ('").append(p.getUniqueId().toString());
		
		for (Stat s : Stat.values()) {
			if (!s.isSaved()) 
				continue;
			
			sql.append(",'").append(stats.get(s)).append("'");
		}
		
		sql.append(") ON DUPLICATE KEY UPDATE uuid=").append(p.getUniqueId().toString());
		
		for (Stat s : Stat.values()) {
			if (!s.isSaved())
				continue;
			
			sql.append(", ").append(s.getId()).append("='").append(stats.get(s)).append("'");
		}
		
		sql.append(";");
		
	    DatabaseConnection con = game.getPool().getConnection();
	    con.executeUpdate(sql.toString(), new ArrayList<>());
	    game.getPool().returnConnection(con);
		
		if (last) {
			stats.clear();
			gp.setStats(stats);
		}
	}
}
