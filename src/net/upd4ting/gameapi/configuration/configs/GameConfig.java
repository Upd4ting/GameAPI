package net.upd4ting.gameapi.configuration.configs;

import net.upd4ting.gameapi.configuration.Configuration;

/**
 * The main game configuration class
 * @author Upd4ting
 *
 */
public class GameConfig extends Configuration {
	
	private boolean bungeeMode;
	private String bungeeLobby;
	private String prefix;
	private int minPlayer;
	private int maxPlayer;
	private int timeStart;
	private int timeStartFull;
	private int timeClose;
	private String bypassPermission;
	private boolean firework;
	private String commandRestart;
	
	private boolean rejoin;
	private int timeToRejoin;
	private int maxRejoin;
	
	private boolean spectatorJoin;
	private String spectatorJoinPermission;
	
	private boolean spectator;
	private int spectatorMaxDistance;
	private String spectatorPermission;
	private boolean spectatorOnlySpecTeammates;
	private boolean spectatorOld;
	
	private boolean chatFormatting;
	
	private boolean mysqlEnabled;
	private String mysqlHost;
	private int mysqlPort;
	private String mysqlDatabase;
	private String mysqlUsername;
	private String mysqlPassword;
	private String mysqlStatTable;

	private String hubItemName;
	private int hubItemId;
	private int hubItemData;
	private int hubItemSlot;
	
	public GameConfig() {
		super(ConfType.STARTUP, "game.yml");
	}

	@Override
	public void loadData() {
		this.bungeeMode = this.conf.getBoolean("bungeeMode");
		this.bungeeLobby = this.conf.getString("bungeeLobby");
		this.prefix = getStringConfig("prefix");
		this.minPlayer = this.conf.getInt("minplayer");
		this.maxPlayer = this.conf.getInt("maxplayer");
		this.timeStart = this.conf.getInt("timestart");
		this.timeStartFull = this.conf.getInt("timestartfull");
		this.timeClose = this.conf.getInt("timeclose");
		this.bypassPermission = this.conf.getString("bypasspermission");
		this.firework = this.conf.getBoolean("firework");
		this.commandRestart = this.conf.getString("commandRestart");
		this.rejoin = this.conf.getBoolean("Rejoin.enabled");
		this.timeToRejoin = this.conf.getInt("Rejoin.time-max");
		this.maxRejoin = this.conf.getInt("Rejoin.max");
		this.spectatorJoin = this.conf.getBoolean("SpectatorJoin.enabled");
		this.spectatorJoinPermission = this.conf.getString("SpectatorJoin.permission");
		this.spectator = this.conf.getBoolean("Spectator.enabled");
		this.spectatorMaxDistance = this.conf.getInt("Spectator.maxDistance");
		this.spectatorPermission = this.conf.getString("Spectator.permission");
		this.spectatorOnlySpecTeammates = this.conf.getBoolean("Spectator.onlySpecTeammates");
		this.spectatorOld = this.conf.getBoolean("Spectator.old");
		this.chatFormatting = this.conf.getBoolean("chatformatting");
		
		this.mysqlEnabled = this.conf.getBoolean("mysqlEnabled");
		this.mysqlHost = this.conf.getString("mysqlHost");
		this.mysqlPort = this.conf.getInt("mysqlPort");
		this.mysqlDatabase = this.conf.getString("mysqlDatabase");
		this.mysqlUsername = this.conf.getString("mysqlUsername");
		this.mysqlPassword = this.conf.getString("mysqlPassword");
		this.mysqlStatTable = this.conf.getString("mysqlStatTable");

		this.hubItemName = getStringConfig("HubItem.name");
		this.hubItemId = this.conf.getInt("HubItem.id");
		this.hubItemData = this.conf.getInt("HubItem.data");
		this.hubItemSlot = this.conf.getInt("HubItem.slot");
	}
	
	public boolean isBungeeMode() {
		return bungeeMode;
	}

	public String getBungeeLobby() {
		return bungeeLobby;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public int getTimeStart() {
		return timeStart;
	}

	public int getTimeStartFull() {
		return timeStartFull;
	}

	public int getTimeClose() {
		return timeClose;
	}

	public String getBypassPermission() {
		return bypassPermission;
	}

	public boolean isFirework() {
		return firework;
	}
	
	public String getCommandRestart() {
		return commandRestart;
	}

	public boolean isRejoin() {
		return rejoin;
	}

	public int getTimeToRejoin() {
		return timeToRejoin;
	}

	public int getMaxRejoin() {
		return maxRejoin;
	}

	public boolean isSpectatorJoin() {
		return spectatorJoin;
	}

	public String getSpectatorJoinPermission() {
		return spectatorJoinPermission;
	}

	public boolean isSpectator() {
		return spectator;
	}

	public int getSpectatorMaxDistance() {
		return spectatorMaxDistance;
	}

	public String getSpectatorPermission() {
		return spectatorPermission;
	}

	public boolean isSpectatorOnlySpecTeammates() {
		return spectatorOnlySpecTeammates;
	}

	public boolean isSpectatorOld() {
		return spectatorOld;
	}

	public boolean isChatFormatting() {
		return chatFormatting;
	}
	
	public boolean isMysqlEnabled() {
		return mysqlEnabled;
	}
	
	public String getMysqlHost() {
		return mysqlHost;
	}
	
	public int getMysqlPort() {
		return mysqlPort;
	}
	
	public String getMysqlDatabase() {
		return mysqlDatabase;
	}
	
	public String getMysqlUsername() {
		return mysqlUsername;
	}
	
	public String getMysqlPassword() {
		return mysqlPassword;
	}
	
	public String getMysqlStatTable() {
		return mysqlStatTable;
	}

	public String getHubItemName() {
		return hubItemName;
	}

	public int getHubItemId() {
		return hubItemId;
	}

	public int getHubItemData() {
		return hubItemData;
	}

	public int getHubItemSlot() {
		return hubItemSlot;
	}
}
