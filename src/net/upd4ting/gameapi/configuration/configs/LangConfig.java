package net.upd4ting.gameapi.configuration.configs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.configuration.Configuration;
import net.upd4ting.gameapi.team.Team;

public class LangConfig extends Configuration {
	
	private String joinNotReady;
	private String joinFull;
	private String joinIngame;
	private String joinFinish;
	private String joinMessage;
	private String leaveMessage;
	private String prefix;
	private String teamCommandPrefix;
	private List<String> teamCommandHelp;
	private String teamCommandErrorGuiEnabled;
	private String teamCommandErrorIngame;
	private String teamCommandErrorNotInTeam;
	private String teamCommandJoin;
	private String teamCommandLeave;
	private String teamCommandNewOwner;
	private String teamCommandErrorNotOnline;
	private String teamCommandErrorNotOwner;
	private String teamCommandErrorAlreadyInTeam;
	private String teamCommandErrorAlreadyInvited;
	private String teamCommandErrorTeamFull;
	private String teamCommandReceiveInvitation;
	private String teamCommandSendInvitation;
	private String chatPrefixSpectator;
	private String chatPrefixAll;
	private String chatPrefixTeam;
	private String chatPrefixGlobalMessage;
	private String chatFormat;
	private String teamInventoryName;
	private String gameConfigInventoryName;
	private String gameCommandError;
	private String countdownErrorNotEnoughPlayer;
	private String gameTimeStart;
	private String gameNotConfigured;
	private String itemReturnName;
	private List<String> signFormat;
	private String statCommandError;
	private List<String> statMessage;
	private String gameStart;
	private String gameWin;
	private String leaderboardHeader;
	private String spectatorTp;
	private String motdWaiting;
	private String motdIngame;
	private String motdFinish;

	public LangConfig() {
		super(ConfType.STARTUP, "lang.yml");
	}

	@Override
	public void loadData() {
		this.joinNotReady = getStringConfig("joinNotReady");
		this.joinFull = getStringConfig("joinFull");
		this.joinIngame = getStringConfig("joinIngame");
		this.joinFinish = getStringConfig("joinFinish");
		this.joinMessage = getStringConfig("joinMessage");
		this.leaveMessage = getStringConfig("leaveMessage");
		this.prefix = getStringConfig("prefix");
		this.teamCommandPrefix = getStringConfig("teamCommandPrefix");
		this.teamCommandHelp = getStringListConfig("teamCommandHelp");
		this.teamCommandErrorGuiEnabled = getStringConfig("teamCommandErrorGuiEnabled");
		this.teamCommandErrorIngame = getStringConfig("teamCommandErrorIngame");
		this.teamCommandErrorNotInTeam = getStringConfig("teamCommandErrorNotInTeam");
		this.teamCommandJoin = getStringConfig("teamCommandJoin");
		this.teamCommandLeave = getStringConfig("teamCommandLeave");
		this.teamCommandNewOwner = getStringConfig("teamCommandNewOwner");
		this.teamCommandErrorNotOnline = getStringConfig("teamCommandErrorNotOnline");
		this.teamCommandErrorNotOwner = getStringConfig("teamCommandErrorNotOwner");
		this.teamCommandErrorAlreadyInTeam = getStringConfig("teamCommandErrorAlreadyInTeam");
		this.teamCommandErrorAlreadyInvited = getStringConfig("teamCommandErrorAlreadyInvited");
		this.teamCommandErrorTeamFull = getStringConfig("teamCommandErrorTeamFull");
		this.teamCommandReceiveInvitation = getStringConfig("teamCommandReceiveInvitation");
		this.teamCommandSendInvitation = getStringConfig("teamCommandSendInvitation");
		this.chatPrefixSpectator = getStringConfig("chatPrefixSpectator");
		this.chatPrefixAll = getStringConfig("chatPrefixAll");
		this.chatPrefixTeam = getStringConfig("chatPrefixTeam");
		this.chatPrefixGlobalMessage = getStringConfig("chatPrefixTeam");
		this.chatFormat = getStringConfig("chatFormat");
		this.teamInventoryName = getStringConfig("teamInventoryName");
		this.gameConfigInventoryName = getStringConfig("gameConfigInventoryName");
		this.gameCommandError = getStringConfig("gameCommandError");
		this.countdownErrorNotEnoughPlayer = getStringConfig("countdownErrorNotEnoughPlayer");
		this.gameTimeStart = getStringConfig("gameTimeStart");
		this.gameNotConfigured = getStringConfig("gameNotConfigured");
		this.itemReturnName = getStringConfig("itemReturnName");
		this.signFormat = getStringListConfig("signFormat");
		this.statCommandError = getStringConfig("statCommandError");
		this.statMessage = getStringListConfig("statMessage");
		this.gameStart = getStringConfig("gameStart");
		this.gameWin = getStringConfig("gameWin");
		this.leaderboardHeader = getStringConfig("leaderboardHeader");
		this.spectatorTp = getStringConfig("spectatorTp");
		this.motdWaiting = getStringConfig("motdWaiting");
		this.motdIngame = getStringConfig("motdIngame");
		this.motdFinish = getStringConfig("motdFinish");
	}
	
	/**
	 * @return Message when game not ready
	 */
	public String getJoinNotReady() {
		return joinNotReady;
	}
	
	/**
	 * @return Message when game full
	 */
	public String getJoinFull() {
		return joinFull;
	}
	
	/**
	 * @return Message when game is started
	 */
	public String getJoinIngame() {
		return joinIngame;
	}
	
	/**
	 * @return Message when game is finished
	 */
	public String getJoinFinish() {
		return joinFinish;
	}
	
	/**
	 * @param p The player who joined
	 * @return Message when player join the game
	 */
	public String getJoinMessage(Player p) {
		return joinMessage.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @param p The player who left
	 * @return Message when player leave the game
	 */
	public String getLeaveMessage(Player p) {
		return leaveMessage.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @return Prefix of the game
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * @return Get team command prefix
	 */
	public String getTeamCommandPrefix() {
		return teamCommandPrefix;
	}
	
	/**
	 * @return Team command help message
	 */
	public List<String> getTeamCommandHelpMessage() {
		return teamCommandHelp;
	}
	
	/**
	 * @return Team command message error when gui is enabled
	 */
	public String getTeamCommandErrorGuiEnabled() {
		return teamCommandErrorGuiEnabled;
	}
	
	/**
	 * @return Team command message error when ingame
	 */
	public String getTeamCommandErrorIngame() {
		return teamCommandErrorIngame;
	}
	
	/**
	 * @return Team command message error when player try to
	 * leave and is not in a team
	 */
	public String getTeamCommandErrorNotInTeam() {
		return teamCommandErrorNotInTeam;
	}
	
	/**
	 * @param p The player that join the team
	 * @return The message of join
	 */
	public String getTeamCommandJoin(Player p) {
		return teamCommandJoin.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @param p The player that leave the team
	 * @return The message of leave
	 */
	public String getTeamCommandLeave(Player p) {
		return teamCommandLeave.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @param p The new owner of the team
	 * @return The message of new owner in a team
	 */
	public String getTeamCommandNewOwner(Player p) {
		return teamCommandNewOwner.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @param name The name of the player that is not online
	 * @return The message error when player not online
	 */
	public String getTeamCommandErrorNotOnline(String name) {
		return teamCommandErrorNotOnline.replaceAll("%player%", name);
	}
	
	/**
	 * @return Team command message error when not owner and
	 * trying to invite someone
	 */
	public String getTeamCommandErrorNotOwner() {
		return teamCommandErrorNotOwner;
	}
	
	/**
	 * @param p The player that is already in the team
	 * @return Team command message error when player
	 * is already in the team
	 */
	public String getTeamCommandErrorAlreadyInTeam(Player p) {
		return teamCommandErrorAlreadyInTeam.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @param p The player that is already been invited
	 * @return Team command message error when player
	 * already been invited
	 */
	public String getTeamCommandErrorAlreadyInvited(Player p) {
		return teamCommandErrorAlreadyInvited.replaceAll("%player%", p.getName());
	}
	
	/**
	 * @return Team command message error when team
	 * is full!
	 */
	public String getTeamCommandErrorTeamFull() {
		return teamCommandErrorTeamFull;
	}
	
	/**
	 * @param from The player who come from the invitation
	 * @param expireTime Expire time of the invitation
	 * @return Team command message receive invitation
	 */
	public String getTeamCommandReceiveInvitation(Player from, int expireTime) {
		return teamCommandReceiveInvitation.replaceAll("%player%", from.getName()).replaceAll("%expiretime%", Integer.toString(expireTime));
	}
	
	/**
	 * @param to The player that we invite
	 * @param expireTime Expire time of the invitation
	 * @return Team command message send invitation
	 */
	public String getTeamCommandSendInvitation(Player to, int expireTime) {
		return teamCommandSendInvitation.replaceAll("%player%", to.getName()).replaceAll("%expiretime%", Integer.toString(expireTime));
	}
	
	/**
	 * @return Prefix chat of spectatir
	 */
	public String getChatPrefixSpectator() {
		return chatPrefixSpectator;
	}
	
	/**
	 * @return Prefix chat global
	 */
	public String getChatPrefixAll() {
		return chatPrefixAll;
	}
	
	/**
	 * @return Prefix chat team
	 */
	public String getChatPrefixTeam() {
		return chatPrefixTeam;
	}
	
	/**
	 * @return Prefix chat to talk in global message
	 */
	public String getChatPrefixGlobalMessage() {
		return chatPrefixGlobalMessage;
	}

	
	/**
	 * @return Chat format
	 */
	public String getChatFormat() {
		return chatFormat;
	}
	
	/**
	 * @return Team inventory name
	 */
	public String getTeamInventoryName() {
		return teamInventoryName;
	}

	/**
	 * @return Game config inventory name
	 */
	public String getGameConfigInventoryName() {
		return gameConfigInventoryName;
	}
	
	/**
	 * @return Game command error when player is not in a game
	 */
	public String getGameCommandError() {
		return gameCommandError;
	}
	
	/**
	 * @return Get countdown error when not enough player
	 */
	public String getCountdownErrorNotEnoughPlayer() {
		return countdownErrorNotEnoughPlayer;
	}
	
	/**
	 * @param sec Second left to start
	 * @return Message that advertise the start of the game
	 */
	public String getGameTimeStart(int sec) {
		return gameTimeStart.replaceAll("%second%", Integer.toString(sec));
	}

	/**
	 * @return Game not configured message
	 */
	public String getGameNotConfigured() {
		return gameNotConfigured;
	}

	/**
	 * @return Item return name
	 */
	public String getItemReturnName() {
		return itemReturnName;
	}
	
	/**
	 * Get the sign format
	 * @param prefix The prefix of the game
	 * @param motd The motd of this game
	 * @param gameName The game name
	 * @param nbPlayer The number of player
	 * @param maxPlayer The max of player
	 * @return The string array for each line of the sign
	 */
	public String[] getSignFormat(String prefix, String motd, String gameName, int nbPlayer, int maxPlayer) {
		String[] lines = new String[signFormat.size()];
		
		for (int i = 0; i < lines.length; i++) {
			String line = signFormat.get(i);
			line = line.replaceAll("%prefix%", prefix).replaceAll("%motd%", motd).replaceAll("%gamename%", gameName)
					.replaceAll("%nbplayer%", Integer.toString(nbPlayer)).replaceAll("%maxplayer%", Integer.toString(maxPlayer));
			lines[i] = line;
		}
		
		return lines;
	}
	
	/**
	 * @return Stat command player not connected
	 */
	public String getStatCommandError(String player) {
		return statCommandError.replaceAll("%player%", player);
	}
	
	/**
	 * @return The stat message
	 */
	public List<String> getStatMessage(String player) {
		List<String> list = new ArrayList<>();
		
		for (String s : statMessage) {
			list.add(s.replaceAll("%player%", player));
		}
		
		return list;
	}
	
	/**
	 * @return Game start message
	 */
	public String getGameStart() {
		return gameStart;
	}
	
	/**
	 * @param t The team who has winned
	 * @return Game win message
	 */
	public String getGameWin(Team t) {
		StringBuilder sb = new StringBuilder();
		
		for (Player p : t.getPlayers()) {
			sb.append(p.getName()).append(", ");
		}
		
		String s = sb.toString();
		s = s.substring(0, s.length() - 3);
		
		return gameWin.replaceAll("%team%", s);
	}
	
	/**
	 * @return Leaderboard header
	 */
	public String getLeaderboardHeader() {
		return leaderboardHeader;
	}
	
	/**
	 * @return When spectator has been teleported
	 */
	public String getSpectatorTp() {
		return spectatorTp;
	}
	
	/**
	 * @return Motd when game isn't started
	 */
	public String getMotdWaiting() {
		return motdWaiting;
	}
	
	/**
	 * @return Motd when game isn't started
	 */
	public String getMotdIngame() {
		return motdIngame;
	}
	
	/**
	 * @return Motd when game isn't started
	 */
	public String getMotdFinish() {
		return motdFinish;
	}
}
