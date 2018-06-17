package net.upd4ting.gameapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.stats.Stat;
import net.upd4ting.gameapi.team.Team;
import org.bukkit.inventory.Inventory;

/**
 * This class will store all information about a player.
 * 
 * @author Upd4ting
 *
 */
public class GamePlayer {

	private static final Map<UUID, GamePlayer> players = new HashMap<>();

	/**
	 * Method to get data of a player
	 * @param p The player
	 * @return The data
	 */
	public static GamePlayer instanceOf(Player p) {
		if (players.containsKey(p.getUniqueId()))
			return players.get(p.getUniqueId());
		else {
			GamePlayer gp = new GamePlayer(p.getUniqueId());
			gp.onLoad();
			players.put(p.getUniqueId(), gp);
			return gp;
		}
	}

	private final UUID uuid;
	private String lastGame;
	private Team lastGameTeam;
	private long lastLeaveTime;
	private final List<UUID> invitations;
	private Integer selectedKit;
	private Map<Stat, Integer> stats;
	private Integer numberOfRejoin;
	private Location lastGameLocation;
	private Location lastLocation;
	private boolean scoreboard;
	private String inventory;
	private int level;
	private float exp;

	// Custom data
	private Map<String, Object> datas = new HashMap<>();
	
	/**
	 * Default constructor of GamePlayer
	 * @param uuid UUID of the player
	 */
	private GamePlayer(UUID uuid) {
		this.uuid = uuid;
		this.selectedKit = -1;
		this.invitations = new ArrayList<>();
		this.stats = new HashMap<>();
		this.numberOfRejoin = 0;
		this.scoreboard = false;
	}
	
	/**
	 * Method to override if we want to do something at the load of
	 * the player
	 */
	protected void onLoad() {}
	
	public UUID getUuid() {
		return uuid;
	}


	public void setLastGame(Game game) {
		this.lastGame = game.getName();
	}
	
	public String getLastGame() {
		return lastGame;
	}
	
	public void setLastGameTeam(Team team) {
		this.lastGameTeam = team;
	}
	
	public Team getLastGameTeam() {
		return lastGameTeam;
	}
	
	public void setLastLeaveTime(long lastLeaveTime) {
		this.lastLeaveTime = lastLeaveTime;
	}
	
	public long getLastLeaveTime() {
		return lastLeaveTime;
	}
	
	public List<UUID> getInvitations() {
		return invitations;
	}

	public void sendInvitation(Player invited) {
		GamePlayer up = instanceOf(invited);
		up.getInvitations().add(this.getUuid());
	}
	
	public void acceptInvitation(Player inviter) {
		invitations.remove(inviter.getUniqueId());
	}
	
	public Boolean hasInvitation(Player inviter) {
		return invitations.contains(inviter.getUniqueId());
	}

	public Integer getSelectedKit() {
		return selectedKit;
	}

	public void setSelectedKit(Integer selectedKit) {
		this.selectedKit = selectedKit;
	}

	public synchronized Map<Stat, Integer> getStats() {
		return stats;
	}

	public synchronized void setStats(Map<Stat, Integer> stats) {
		this.stats = stats;
	}

	public Integer getNumberOfRejoin() {
		return numberOfRejoin;
	}

	public void setNumberOfRejoin(Integer numberOfRejoin) {
		this.numberOfRejoin = numberOfRejoin;
	}

	public void setLastGameLocation(Location lastGameLocation) {
		this.lastGameLocation = lastGameLocation;
	}

	public Location getLastGameLocation() {
		return lastGameLocation;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void setScoreboard(boolean scoreboard) {
		this.scoreboard = scoreboard;
	}

	public boolean isScoreboard() {
		return scoreboard;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public <T> void setData(String name, T value) {
		this.datas.put(name, value);
	}

	public <T> T getData(String name) {
		return (T) this.datas.get(name);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getExp() {
		return exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}
}
