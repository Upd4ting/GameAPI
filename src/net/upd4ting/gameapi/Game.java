package net.upd4ting.gameapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.upd4ting.gameapi.board.Board;
import net.upd4ting.gameapi.customevents.*;
import net.upd4ting.gameapi.inventory.inv.config.*;
import net.upd4ting.gameapi.specialitems.SpecialItem;
import net.upd4ting.gameapi.util.Reflector;
import net.upd4ting.gameapi.util.UtilInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.upd4ting.gameapi.configuration.configs.GameConfig;
import net.upd4ting.gameapi.databaselib.ConnectionPool;
import net.upd4ting.gameapi.databaselib.DatabaseConnection.ConnectionType;
import net.upd4ting.gameapi.databaselib.DatabaseConnection.Credentials;
import net.upd4ting.gameapi.gamehandler.BoardHandler;
import net.upd4ting.gameapi.gamehandler.SpectatorHandler;
import net.upd4ting.gameapi.gamehandler.handlers.NormalSpectatorHandler;
import net.upd4ting.gameapi.gamehandler.handlers.OldSpectatorHandler;
import net.upd4ting.gameapi.nms.NMSHandler;
import net.upd4ting.gameapi.nms.common.NametagHandler;
import net.upd4ting.gameapi.nms.common.NametagHandler.NameTagColor;
import net.upd4ting.gameapi.nms.common.TitleHandler;
import net.upd4ting.gameapi.stats.Stat;
import net.upd4ting.gameapi.team.Team;
import net.upd4ting.gameapi.trigger.Trigger;
import net.upd4ting.gameapi.util.Util;

/**
 * This class has to be used to make a game.
 * 
 * @author Upd4ting
 *
 */
public abstract class Game {
	
	/**
	 * Annotation to inform that a field is changeable or not
	 * via command
	 * @author Upd4ting
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Changeable {
		String config();
		String redirect() default "";
		boolean required() default false;
	}
	
	// Information about the game...
	private String name;

	private World world;

	@Changeable(config = "configlang.yml", required = true)
	private Location lobby;

	private String prefix;

	private GameState state;

	private boolean ready;

	private boolean ffa;

	@Changeable(config = "configlang.yml", required = true)
	private Integer minPlayerToStart;

	@Changeable(config = "configlang.yml", required = true)
	private Integer maxPlayers;

	@Changeable(config = "configlang.yml", required = true)
	private Integer timeToStart;

	@Changeable(config = "configlang.yml", required = true)
	private Integer timeToStartFull;

	private Countdown countdown;


	private SpectatorHandler specHandler;

	private BoardHandler boardHandler;

	private GameSnapshot snapshot;


	private boolean sqlEnabled;

	private String statTable;

	private Credentials cred;

	private ConnectionPool pool;

	
	private List<Trigger> triggers;


	@Changeable(config = "configlang.yml")
	private boolean chatFormatting;

	@Changeable(config = "configlang.yml")
	private boolean fireworkEnabled;

	
	private int teamSize;

	private boolean friendlyFire;

	private boolean autoFillTeam;

	private int expireTime;

	private TeamJoiningMode teamJoiningMode;

	
	private int timeToRespawn;


	@Changeable(config = "configlang.yml")
	private boolean rejoinEnabled;

	@Changeable(config = "configlang.yml")
	private int timeToRejoin;

	@Changeable(config = "configlang.yml")
	private int maxRejoin;


	@Changeable(config = "configlang.yml", required = true)
	private Integer timeToClose;


	@Changeable(config = "configlang.yml")
	private boolean spectatorJoin;

	private String spectatorJoinPermission;


	@Changeable(config = "configlang.yml")
	private boolean spectator;

	private String spectatorPermission;

	@Changeable(config = "configlang.yml")
	private boolean spectatorOnlyMate;

	@Changeable(config = "configlang.yml")
	private int spectatorDistance;

	
	private String bypassMaxPermission;

	
	private SubcommandHandler commandHandler;

	
	private int time;

	private GameTask gameTask;

	
	// Information about the players
	private List<Player> playing = new ArrayList<>();
	private final List<Player> insides = new ArrayList<>();
	private final List<Player> spectators = new ArrayList<>();
	private List<Team> teams = new ArrayList<>();

	/**
	 * Constructor for serialization purpose
	 */
	public Game() {}
	
	/**
	 * Default constructor of game
	 */
	public Game(World world, String name) {
		this.world = world;
		this.name = name;
		this.ready = false;
		this.state = GameState.WAITING;
		this.teamJoiningMode = TeamJoiningMode.GUI;
		this.spectatorJoin = false;
		this.spectatorOnlyMate = true;
		this.spectatorDistance = 50;
		this.timeToRejoin = -1;
		this.rejoinEnabled = false;
		this.maxRejoin = 1;
		this.spectator = false;
		this.autoFillTeam = false;
		this.timeToRespawn = 5;
		this.timeToClose = 10;
		this.expireTime = 30;
		this.chatFormatting = true;
		this.timeToStart = 10;
		this.timeToStartFull = 3;
		this.countdown = Countdown.newInstance(this);
		this.time = 0;
		this.gameTask = GameTask.newInstance(this);
		this.triggers = new ArrayList<>();
		this.ffa = true;
		this.teamSize = 1;
		this.friendlyFire = false;
		this.teamJoiningMode = TeamJoiningMode.COMMAND;
		this.snapshot = new GameWorldSnapshot();
		
		// We init to default one
		initDefaultSettings();
	}

	/**
	 * Function called when game is rebooted or instanciated
	 */
	public abstract void load();
	
	protected void initDefaultSettings() {
		GameConfig gameConfig = GameAPI.getGameConfiguration();
		
		this.prefix = gameConfig.getPrefix();
		this.minPlayerToStart = gameConfig.getMinPlayer();
		this.maxPlayers = gameConfig.getMaxPlayer();
		this.timeToStart = gameConfig.getTimeStart();
		this.timeToStartFull = gameConfig.getTimeStartFull();
		this.timeToClose = gameConfig.getTimeClose();
		
		if (!gameConfig.getBypassPermission().equals("none"))
			this.bypassMaxPermission = gameConfig.getBypassPermission();
		
		this.fireworkEnabled = gameConfig.isFirework();
		this.rejoinEnabled = gameConfig.isRejoin();
		this.timeToRejoin = gameConfig.getTimeToRejoin();
		this.maxRejoin = gameConfig.getMaxRejoin();
		
		this.spectatorJoin = gameConfig.isSpectatorJoin();
		
		if (!gameConfig.getSpectatorJoinPermission().equals("none"))
			this.spectatorJoinPermission = gameConfig.getSpectatorJoinPermission();
		
		this.spectator = gameConfig.isSpectator();
		this.spectatorDistance = gameConfig.getSpectatorMaxDistance();
		
		if (!gameConfig.getSpectatorPermission().equals("none"))
			this.spectatorPermission = gameConfig.getSpectatorPermission();
		
		this.spectatorOnlyMate = gameConfig.isSpectatorOnlySpecTeammates();
		
		if (gameConfig.isSpectatorOld())
			this.specHandler = new OldSpectatorHandler();
		else
			this.specHandler = new NormalSpectatorHandler();
		
		this.cred = new Credentials(gameConfig.getMysqlHost(), gameConfig.getMysqlPort(), gameConfig.getMysqlDatabase(), gameConfig.getMysqlUsername(), gameConfig.getMysqlPassword());
		this.pool = new ConnectionPool(ConnectionType.SQL, this.cred);
		this.sqlEnabled = gameConfig.isMysqlEnabled();
		this.statTable = gameConfig.getMysqlStatTable();
		
		this.chatFormatting = gameConfig.isChatFormatting();

		// To make him take count of new time to start from config
		this.countdown.reset();

		load();

		// Load saved settings
		GameSaver.load(this);
	}
	
	/**
	 * This method is called to know if the player
	 * can join the game
	 * @param p The player
	 * @return The joinResult
	 */
	public JoinResult canJoin(Player p) {
		if (!this.ready) {
			String message = GameAPI.getLangConfiguration().getJoinNotReady();
			return new JoinResult(false, message);
		}

		if (this.isWaiting()) {
			if (this.getFreeSlot() <= 0 && (this.bypassMaxPermission == null || !p.hasPermission(this.bypassMaxPermission))) { // If enough free slot
				String message = GameAPI.getLangConfiguration().getJoinFull();
				return new JoinResult(false, message);
			}
		} else if (this.isInGame()) {			
			if (!this.isPlayerUseRejoin(p)) { // Rejoin haven't worked
				if (!this.isPlayerUseSpectatorJoin(p)) { // SpectatorJoin haven't worked
					String message = GameAPI.getLangConfiguration().getJoinIngame();
					return new JoinResult(false, message);
				}
			}
		} else if (this.isFinished()) {
			String message = GameAPI.getLangConfiguration().getJoinFinish();
			return new JoinResult(false, message);
		}
		
		return new JoinResult(true);
	}
	
	/**
	 * This method is called when a player join the game
	 * He can join the game if bungee when connecting on the server
	 * Otherwise when using a sign
	 * @param p The player
	 */
	public void join(Player p) {		
		this.insides.add(p);

		GamePlayer gp = GamePlayer.instanceOf(p);
		gp.setLastLocation(p.getLocation());

		// Save his inventory
		gp.setInventory(UtilInventory.toBase64(p.getInventory()));
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.updateInventory();

		// Save and reset level & exp
		gp.setLevel(p.getLevel());
		gp.setExp(p.getExp());
		p.setLevel(0);
		p.setExp(0);

		// Reset health and food level
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		
		if (this.isPlayerUseRejoin(p)) {
			this.playing.add(p);

			Team t = gp.getLastGameTeam();
			
			if (!t.isAlive()) {
				this.expulse(p, "Your team is no longer alive");
				return;
			}
			
			t.join(p);

			p.teleport(gp.getLastGameLocation()); // We tp him back to his location
			
			gp.setNumberOfRejoin(gp.getNumberOfRejoin() + 1);
		} else if (this.isPlayerUseSpectatorJoin(p)) {
			this.spectators.add(p);
			
			if (this.specHandler != null)
				this.specHandler.setSpectator(this, p);

			this.giveReturnToLobby(p);
		} else {
			// Normal join so we tp to lobby
			p.teleport(this.lobby != null ? this.lobby : Util.getHighestLoc(this.world.getSpawnLocation()));

			this.giveReturnToLobby(p);
		}
		
		// We call the listener
		Bukkit.getPluginManager().callEvent(new GameJoinEvent(p, this));
		
		this.sendConfigAll(GameAPI.getLangConfiguration().getJoinMessage(p));

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!this.insides.contains(pl)) {
				p.hidePlayer(pl);
				pl.hidePlayer(p);
			}
		}
		
		// Load stat
		Stat.load(this, p);

		// Start board
		Board.getInstance().init(this, p);
		
		// Check for countdown
		if (this.isWaiting() && !this.countdown.isStarted() && this.getMinPlayerToStart() <= this.getInsides().size()) {
			this.countdown.start(false);
		}
	}
	
	/**
	 * The method is called when a player leave the game
	 * He can leave the game with a command /leave, hub, etc or with
	 * items of leave
	 * @param p The player who left
	 */
	public void leave(Player p) {
		this.insides.remove(p);
		this.playing.remove(p);
		this.spectators.remove(p);

		GamePlayer gp = GamePlayer.instanceOf(p);
		
		if (this.isWaiting()) {
			this.sendConfigAll(GameAPI.getLangConfiguration().getLeaveMessage(p));
		}
		
		if (this.isInGame()) {
			Team t = this.getTeam(p);
			
			gp.setLastGame(this);
			gp.setLastGameTeam(t);
			gp.setLastGameLocation(p.getLocation());
			gp.setLastLeaveTime(System.currentTimeMillis());
			
			// We remove him from his team
			t.getPlayers().remove(p);

			this.checkWin();
		}
		

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!this.insides.contains(pl)) {
				p.showPlayer(pl);
				pl.showPlayer(p);
			}
		}

		restoreState(p);

		// Stop board
		Board.getInstance().leave(p);
		
		Bukkit.getPluginManager().callEvent(new GameLeaveEvent(p, this));
	}
	
	/**
	 * Tell that the game is fully load up and that she is ready
	 */
	public void ready() {
		// Load the SQL table
		Stat.loadTable(this);
				
		ready = true;
	}
	
	/**
	 * Add a trigger to the game
	 * @param trigger The trigger
	 */
	public void trigger(Trigger trigger) {
		triggers.add(trigger);
	}
	
	/**
	 * Start the game
	 * Will call game#spawn for each player
	 */
	public void start() {
		if (!checkConfiguration()) { // Game not configured
			this.countdown.reset();
			this.avertAll(GameAPI.getLangConfiguration().getGameNotConfigured());
			return;
		}

		// Save du world (we can do that async to not lag everything)
		final Game game = this;

		new BukkitRunnable() {
			@Override
			public void run() {
				game.snapshot.save(game);
			}
		}.runTaskAsynchronously(GameAPI.getInstance());

		// Save game configuration
		GameSaver.save(this);

		// Save insides in playing
		playing = new ArrayList<>(insides);
		
		// Team fill & nametag
		this.fillTeam();
		if (this.getTeamJoiningMode() != TeamJoiningMode.GUI)
			this.setNameTag();
		
		// Spawn players
		for (Player pl : playing) {
			this.spawn(pl, false);
		}
		
		// Start game task
		gameTask.start();
		
		// Message start
		this.avertAll(GameAPI.getLangConfiguration().getGameStart());
		
		state = GameState.INGAME;
		
		// Call start listener
		GameStartEvent event = new GameStartEvent(this);
		Bukkit.getPluginManager().callEvent(event);
	}

	/**
	 * Check if the game is configured well
	 */
	private boolean checkConfiguration() {
		boolean configured = true;

		List<Field> fields = new ArrayList<>();
		Reflector.getAllFields(fields, this.getClass());
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Game.Changeable.class))
				continue;

			Changeable c = field.getAnnotation(Changeable.class);

			if (!c.required())
				continue;

			field.setAccessible(true);

			try {
				if (field.get(this) == null)
					configured = false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return configured;
	}
	
	/**
	 * Check if the game is ended and that we have a winner
	 * Will be called when a player leave and when a player lose
	 */
	private void checkWin() {
		if (this.getTeamsAlive() == 1) {
			Player p = this.playing.get(0);
			Team t = this.getTeam(p);
			this.win(t);
		} else if (this.getTeamsAlive() == 0) {
			this.close(true);
		}
	}
	
	/**
	 * Called when a Team win!
	 * Can be called by game#checkWin or a trigger
	 * @param t Team that won
	 */
	private void win(Team t) {
		this.avertAll(GameAPI.getLangConfiguration().getGameWin(t));
		
		if (this.fireworkEnabled)
			new FireworkTask(t.getPlayers()).runTaskTimer(GameAPI.getInstance(), 0, 15);

		for (Player p : t.getPlayers())
			Stat.increase(p, Stat.WIN, 1);

		for (Player p : this.getInsides()) {
			if (t.getPlayers().contains(p))
				Stat.increase(p, Stat.WIN, 1);
			else
				Stat.increase(p, Stat.GAMELOSE, 1);

			Stat.increase(p, Stat.GAMEPLAYED, 1);
		}
		
		this.close(true);
	}
	
	/**
	 * Make the game close
	 */
	public void close(boolean wait) {
		// Finish state
		state = GameState.FINISH;
		
		// Display leaderboard
		displayLeaderboard();
		
		// Stop game task
		gameTask.stop();
		
		// Saving
		Stat.save(this, true);
		
		// Closing
		if (wait) {
			Game game = this;

			new BukkitRunnable() {
				@Override
				public void run() {
					game.expulseAllAndReboot();
				}
			}.runTaskLater(GameAPI.getInstance(), game.timeToClose * 20);
		} else {
			this.expulseAllAndReboot();
		}
	}

	private void expulseAllAndReboot() {
		for (Player p : this.getInsides())
			this.expulse(p, "Game finished");

		if (GameAPI.getGameConfiguration().isBungeeMode())
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GameAPI.getGameConfiguration().getCommandRestart());
		else
			reboot();
	}

	private void reboot() {
		// Restore the world
		snapshot.restore(this);

		// Ready is now again to false so
		ready = false;

		// Restore list
		insides.clear();
		spectators.clear();
		playing.clear();

		// Restore teams if not auto created
		if (teamJoiningMode == TeamJoiningMode.COMMAND)
			teams.clear();

		// We reset the countdown
		countdown.reset();

		// Reset the time
		gameTask.stop();
		time = 0;

		// Reset triggers
		triggers.clear();

		// We call 'load' again cause plugin using
		// API may have some things to reset
		load();

		// Restore state
		state = GameState.WAITING;
	}
	
	private void displayLeaderboard() {
		List<Player> players = new ArrayList<>(this.insides);
		players.sort(killHightFirst);
		
		// Display some void message
		for (int i = 0; i <= 2; i++)
			this.sendMessageAll("  ");
		
		// Display header
		this.sendConfigAll(GameAPI.getLangConfiguration().getLeaderboardHeader());
		
		for (int i = 0; i < 3 && i < players.size(); i++) {
			Player p = players.get(i);
			this.sendMessageAll(ChatColor.GRAY + "" + (i+1) + ". " + ChatColor.YELLOW + p.getName()
					+ ChatColor.RED + " " + Stat.KILLGAME.get(p));
		}
	}
	
	private final Comparator<Player> killHightFirst = (p1, p2) -> Stat.KILLGAME.get(p2) - Stat.KILLGAME.get(p1);
	
	/**
	 * Called when a player die
	 * @param p Player who died
	 * @param killer Player who killed (can be null if not died by a player)
	 */
	public boolean die(Player p, Player killer) {
		Stat.increase(p, Stat.DEATH, 1);

		if (killer != null) {
			Stat.increase(killer, Stat.KILL, 1);
			Stat.increase(killer, Stat.KILLGAME, 1);
		}

		// Call listener
		GameDieEvent event = new GameDieEvent(p, this);
		Bukkit.getPluginManager().callEvent(event);

		for (Trigger t : triggers) {
			boolean losed = t.onDie(this, p);
			
			if (losed) {
				this.lose(p);
				return true;
			}
		}

		// We make it spectator just for the respawn time
		if (this.specHandler != null)
			this.specHandler.setSpectator(this, p);
		
		// Function hasn't returned so we respawn it
		this.respawn(p);
		return false;
	}
	
	/**
	 * Called to respawn a player
	 * Will be called by game#die if the player
	 * haven't lose
	 * @param p The player that is respawned
	 */
	public void respawn(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				spawn(p, true);
			}
		}.runTaskLater(GameAPI.getInstance(), 20L * this.timeToRespawn);
	}
	
	/**
	 * Called when a player will spawn into the game
	 * @param p Player that is spawned
	 * @param respawn Respawned or not
	 */
	public void spawn(Player p, boolean respawn) {
		// We clear the inventory
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.updateInventory();
		p.closeInventory();
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setLevel(0);
		p.setExp(0);
		
		// Spawn the player
		GameSpawnEvent event = new GameSpawnEvent(p, this, respawn);
		Bukkit.getPluginManager().callEvent(event);
	}
	
	/**
	 * Called when a player lose the game
	 * @param p The player
	 */
	private void lose(Player p) {
		this.playing.remove(p);

		// Call listener
		GameLoseEvent event = new GameLoseEvent(p, this);
		Bukkit.getPluginManager().callEvent(event);
		
		if (!this.spectator || this.spectatorPermission == null || !p.hasPermission(this.spectatorPermission)) {
			new BukkitRunnable() {
				@Override
				public void run() {
					expulse(p, "You lost");
				}
			}.runTaskLater(GameAPI.getInstance(), 60);
			this.checkWin();
			return;
		}

		// Spectator handling
		if (this.specHandler != null)
			this.specHandler.setSpectator(this, p);

		this.spectators.add(p);

		this.checkWin();
	}
	
	public void giveReturnToLobby(Player p) {
		ItemStack stack = SpecialItem.get(GameAPI.getHubItemId()).getClonedItem();
		p.getInventory().setItem(GameAPI.getGameConfiguration().getHubItemSlot(), stack);
		p.updateInventory();
	}
	
	public void expulse(Player p, String message) {
		if (message != null)
			p.sendMessage(message);

		leave(p);

		if (GameAPI.getGameConfiguration().isBungeeMode()) {
			if (GameAPI.getGameConfiguration().getBungeeLobby() == null)
				return;

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(GameAPI.getGameConfiguration().getBungeeLobby());
			p.sendPluginMessage(GameAPI.getInstance(), "BungeeCord", out.toByteArray());
		} else {
			GamePlayer gp = GamePlayer.instanceOf(p);
			p.teleport(gp.getLastLocation());
			gp.setLastLocation(null);
		}
	}

	public void restoreState(Player p) {
		GamePlayer gp = GamePlayer.instanceOf(p);

		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

		// We restore his inventory
		UtilInventory.fromBase64(p.getInventory(), gp.getInventory());
		p.updateInventory();

		// Restore exp and level
		p.setLevel(gp.getLevel());
		p.setExp(gp.getExp());

		// Restore basic things
		p.setGameMode(GameMode.SURVIVAL);
		p.setFlying(false);
		p.setAllowFlight(false);
	}
	
	/**
	 * Method to override to createTeams of the game
	 * Used when teamJoiningMode is GUI
	 * @return The list of team
	 */
	public List<Team> createTeams() {
		return null;
	}
	
    /**
     * Send an alert to all the player
     * @param message The message
     */
	public void avertAll(String message) {
		for (Player p : this.getInsides())
			avert(p, message);
	}
	
	/**
	 *  Send and alert to a player
	 * @param p The player
	 * @param message The message
	 */
	public void avert(final Player p, final String message) {
		sendActionConfigMessage(message, () -> {
            p.sendMessage(GameAPI.getLangConfiguration().getPrefix() + message);

            TitleHandler handler = (TitleHandler) NMSHandler.getModule("TitleUtils");

            if (handler == null)
            	return;

            handler.sendTitle(p, "", message);
        });
	}
	
	/**
	 * Send a config message to all players of the game
	 * with the prefix of the game
	 * @param message The message
	 */
	public void sendConfigAll(final String message) {
		for (Player p : this.getInsides())
			this.sendConfigMessage(p, message);
	}
	
	/**
	 * Send a config message to a player with the game prefix
	 * @param player The player
	 * @param message The message
	 */
	public void sendConfigMessage(final Player player, final String message) {
		sendActionConfigMessage(message, () -> player.sendMessage(GameAPI.getLangConfiguration().getPrefix() + message));
	}
	
	/**
	 * Send a config message to a player with a custom prefix
	 * @param prefix Prefix of the message
	 * @param player The player
	 * @param message The message
	 */
	public void sendConfigMessage(final String prefix, final Player player, final String message) {
		sendActionConfigMessage(message, () -> player.sendMessage(prefix + message));
	}
	
	/**
	 * Use to send a message if not disabled
	 * @param message Message to send
	 * @param action Action to send the message
	 * @return If the message has been sended or not (true/false)
	 */
	public Boolean sendActionConfigMessage(String message, ActionMessage action) {
		if (!message.equals("DISABLED"))
			action.run();
		return !message.equals("DISABLED");
	}
	
	/**
	 * Interface for message sending
	 * @author Upd4ting
	 *
	 */
	private interface ActionMessage { void run(); }
	
	/**
	 * Play a sound to every player of the game
	 * @param nameSound The sound
	 */
	public void playSoundToAll(String nameSound) {
		for (Player p : this.getInsides())
			playSound(p, nameSound);
	}
	
	/**
	 * Play a sound to a player
	 * @param p The player
	 * @param nameSound The sound
	 */
	public void playSound(Player p, String nameSound) {
		try {
			Field f = Sound.class.getDeclaredField(nameSound);
			f.setAccessible(true);
			Sound sound = (Sound) f.get(null);
			p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
		} catch (IllegalAccessException|IllegalArgumentException|NoSuchFieldException e) { e.printStackTrace(); }
	}

	/**
	 * Send message to all players
	 * @param message The message.
	 */
	public void sendMessageAll(String message) {
		for (Player pl : this.getInsides())
			pl.sendMessage(message);
	}
	
	/**
	 * Know if player is doing a spectator join
	 * @param p The player
	 * @return true/false
	 */
	public boolean isPlayerUseSpectatorJoin(Player p) {
		return this.isInGame() && this.spectatorJoin && (this.spectatorJoinPermission == null || p.hasPermission(this.spectatorJoinPermission));
	}
	
	/**
	 * Know if player is doing a rejoin
	 * @param p The player
	 * @return true/false
	 */
	public boolean isPlayerUseRejoin(Player p) {
		GamePlayer gp = GamePlayer.instanceOf(p);

		boolean result =  this.isInGame() && this.rejoinEnabled && gp.getLastGame() != null && gp.getLastGame().equals(this.name) &&
				gp.getNumberOfRejoin() < this.maxRejoin && 
				((System.currentTimeMillis() - gp.getLastLeaveTime()) / 1000 <= this.timeToRejoin);

		GameRejoinEvent event = new GameRejoinEvent(p, this);
		Bukkit.getPluginManager().callEvent(event);

		return result && !event.isCancelled();
	}
	
	/**
	 * Get if the game is ready or not
	 * @return @ready
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Get if the game is FFA or not
	 * @return The result
	 */
	public boolean isFFA() {
		return ffa;
	}

	/**
	 * Set FFA
	 * @param ffa If ffa or not
	 */
	public void setFFA(boolean ffa) {
		this.ffa = ffa;
	}

	/**
	 * Get prefix of the game
	 * @return The prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Get the minimum amount of player to start the game
	 * @return The value
	 */
	public Integer getMinPlayerToStart() {
		return minPlayerToStart;
	}
	
	/**
	 * Set the minimum amount of player to start the game
	 * @param min The value
	 */
	public void setMinPlayerToStart(int min) {
		this.minPlayerToStart = min;
	}
	
	/**
	 * Get maxPlayers of the game
	 * @return The maxPlayers of the game
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	/**
	 * @return Time to start
	 */
	public int getTimeToStart() {
		return timeToStart;
	}
	
	/**
	 * Set time to start
	 * @param timeToStart Time to start
	 */
	public void setTimeToStart(int timeToStart) {
		this.timeToStart = timeToStart;
	}
	
	/**
	 * @return Time to start full
	 */
	public int getTimeToStartFull() {
		return timeToStartFull;
	}
	
	/**
	 * Set time to start full
	 * @param timeToStartFull Time to start full
	 */
	public void setTimeToStartFull(int timeToStartFull) {
		this.timeToStartFull = timeToStartFull;
	}
	
	/**
	 * @return The countdown of the game
	 */
	public Countdown getCountdown() {
		return countdown;
	}
	
	/**
	 * Set Spectator handler
	 * @param specHandler Spectator handler
	 */
	public void setSpecHandler(SpectatorHandler specHandler) {
		this.specHandler = specHandler;
	}
	
	/**
	 * @return Spectator handler
	 */
	public SpectatorHandler getSpecHandler() {
		return specHandler;
	}
	
	/**
	 * Set Board handler
	 * @param boardHandler Board handler
	 */
	public void setBoardHandler(BoardHandler boardHandler) {
		this.boardHandler = boardHandler;
	}
	
	/**
	 * @return The countdown of the game
	 */
	public BoardHandler getBoardHandler() {
		return boardHandler;
	}

	/**
	 * Set game snapshot
	 * @param snapshot The snapshot
	 */
	public void setSnapshot(GameSnapshot snapshot) {
		this.snapshot = snapshot;
	}

	/**
	 * @return The game snapshot
	 */
	public GameSnapshot getSnapshot() {
		return snapshot;
	}

	/**
	 * @return SQL enabled or not
	 */
	public boolean isSqlEnabled() {
		return sqlEnabled;
	}
	
	/**
	 * @return SQL stat table
	 */
	public String getStatTable() {
		return statTable;
	}
	
	/**
	 * @return Mysql credentials
	 */
	public Credentials getCred() {
		return cred;
	}
	
	/**
	 * @return Mysql connection pool
	 */
	public ConnectionPool getPool() {
		return pool;
	}

	/**
	 * Set the chat formatting or not
	 * @param chat true/false
	 */
	public void setChatFormatting(boolean chat) {
		this.chatFormatting = chat;
	}
	
	/**
	 * @return Wether the chat formatting is enable or not
	 */
	public boolean getChatFormatting() {
		return chatFormatting;
	}
	
	/**
	 * @return If firework is enabled
	 */
	public boolean isFireworkEnabled() {
		return fireworkEnabled;
	}
	
	/**
	 * Set firework enabled or not
	 * @param fireworkEnabled The value
	 */
	public void setFireworkEnabled(boolean fireworkEnabled) {
		this.fireworkEnabled = fireworkEnabled;
	}

	/**
	 * @return The expire time of the invitation in team
	 */
	public int getExpireTime() {
		return expireTime;
	}
	
	/**
	 * Set the expire time of invitation in team
	 * @param expireTime The value
	 */
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
	
	/**
	 * Get team size of the game
	 * @return The value
	 */
	public int getTeamSize() {
		return teamSize;
	}
	
	/**
	 * Set teamSize of the game
	 * @param teamSize The team size
	 */
	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}

	/**
	 * @return If friendly fire is enabled
	 */
	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	/**
	 * Set friendly fire.
	 * @param friendlyFire The value.
	 */
	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	/**
	 * Get the team joining mode
	 * @return The teamJoiningMode
	 */
	public TeamJoiningMode getTeamJoiningMode() {
		return teamJoiningMode;
	}

	/**
	 * Set the team joining mode
	 */
	public void setTeamJoiningMode(TeamJoiningMode mode) {
		if (mode == TeamJoiningMode.COMMAND)
			this.teams = new ArrayList<>();
		else
			this.teams = this.createTeams();
	}
	
	/**
	 * Set time to respawn in second.
	 * @param timeToRespawn The time
	 */
	public void setTimeToRespawn(int timeToRespawn) {
		this.timeToRespawn = timeToRespawn;
	}
	
	/**
	 * @return Time to respawn
	 */
	public int getTimeToRespawn() {
		return timeToRespawn;
	}
	
	/**
	 * Set time to rejoin
	 * @param timeToRejoin The time to rejoin
	 */
	public void setTimeToRejoin(int timeToRejoin) {
		this.timeToRejoin = timeToRejoin;
	}
	
	/**
	 * Get time to rejoin
	 * @return The time to rejoin
	 */
	public int getTimeToRejoin() {
		return timeToRejoin;
	}
	
	/**
	 * @return If rejoin is enabled
	 */
	public boolean isRejoinEnabled() {
		return rejoinEnabled;
	}
	
	/**
	 * Set if rejoin is enabled
	 * @param rejoinEnabled true/false
	 */
	public void setRejoinEnabled(boolean rejoinEnabled) {
		this.rejoinEnabled = rejoinEnabled;
	}
	
	/**
	 * @return The max time of rejoin a player can do
	 */
	public int getMaxRejoin() {
		return maxRejoin;
	}
	
	/**
	 * Set the max time of rejoin a player can do
	 * @param maxRejoin The value
	 */
	public void setMaxRejoin(int maxRejoin) {
		this.maxRejoin = maxRejoin;
	}

	/**
	 * Set time to close of the game after the end
	 * @param timeToClose The value
	 */
	public void setTimeToClose(int timeToClose) {
		this.timeToClose = timeToClose;
	}
	
	/**
	 * Get time to close of the game after the end
	 * @return The value
	 */
	public int getTimeToClose() {
		return timeToClose;
	}
	
	/**
	 * Set auto fill team
	 * @param autoFillTeam true/false
	 */
	public void setAutoFillTeam(boolean autoFillTeam) {
		this.autoFillTeam = autoFillTeam;
	}
	
	/**
	 * Get if auto fill is enabled
	 * @return true/false
	 */
	public boolean isAutoFillTeam() {
		return autoFillTeam;
	}
	
	/**
	 * Set spectator join
	 * @param spectatorJoin The value
	 */
	public void setSpectatorJoin(boolean spectatorJoin) {
		this.spectatorJoin = spectatorJoin;
	}
	
	/**
	 * Get spectator join
	 * @return The value
	 */
	public boolean getSpectatorJoin() {
		return spectatorJoin;
	}
	
	/**
	 * Set spectator join permission
	 * @param spectatorJoinPermission The permission to set
	 */
	public void setSpectatorJoinPermission(String spectatorJoinPermission) {
		this.spectatorJoinPermission = spectatorJoinPermission;
	}
	
	/**
	 * Get spectator join permission
	 * @return The permission
	 */
	public String getSpectatorJoinPermission() {
		return this.spectatorJoinPermission;
	}
	
	/**
	 * @return If spectator can only spectate their mates
	 */
	public boolean isSpectatorOnlyMate() {
		return spectatorOnlyMate;
	}
	
	/**
	 * Set wether spectator can only spectate their mates
	 * or not.
	 * @param spectatorOnlyMate The value
	 */
	public void setSpectatorOnlyMate(boolean spectatorOnlyMate) {
		this.spectatorOnlyMate = spectatorOnlyMate;
	}
	
	/**
	 * @return Distance from their mate maximum
	 */
	public int getSpectatorDistance() {
		return spectatorDistance;
	}
	
	/**
	 * Set distance from their mate maximum
	 * @param spectatorDistance The distance
	 */
	public void setSpectatorDistance(int spectatorDistance) {
		this.spectatorDistance = spectatorDistance;
	}

	/**
	 * Set spectator
	 * @param spectator The value
	 */
	public void setSpectator(boolean spectator) {
		this.spectator = spectator;
	}
	
	/**
	 * Get spectator
	 * @return The value
	 */
	public boolean getSpectator() {
		return spectator;
	}
	
	/**
	 * Set spectator permission
	 * @param spectatorPermission The permission to set
	 */
	public void setSpectatorPermission(String spectatorPermission) {
		this.spectatorPermission = spectatorPermission;
	}
	
	/**
	 * Get spectator permission
	 * @return The permission
	 */
	public String getSpectatorPermission() {
		return this.spectatorPermission;
	}
	
	/**
	 * Set the permission to be able to join when game is full!
	 * @param bypassMaxPermission The permission
	 */
	public void setBypassMaxPermission(String bypassMaxPermission) {
		this.bypassMaxPermission = bypassMaxPermission;
	}
	
	/**
	 * Get bypass max permission
	 * @return The permission
	 */
	public String getBypassMaxPermission() {
		return bypassMaxPermission;
	}
	
	/**
	 * @return CommandHandler
	 */
	public SubcommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	/**
	 * Set the command handler
	 * @param commandHandler The commandhandler
	 */
	public void setCommandHandler(SubcommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	/**
	 * @return Time of the game
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Set the time of the game
	 * @param time Time of the game
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Get if the game is running or not
	 * @return The result
	 */
	public boolean isInGame() {
		return state == GameState.INGAME;
	}
	
	/**
	 * Get if the game is in waiting or not
	 * @return The result
	 */
	public boolean isWaiting() {
		return state == GameState.WAITING;
	}
	
	/**
	 * Get if the game is finised or not
	 * @return The result
	 */
	public boolean isFinished() {
		return state == GameState.FINISH;
	}
	
	/**
	 * Get the status of the game
	 * @return Status of the game
	 */
	public GameState getState() {
		return state;
	}
	
	/**
	 * Return a copy of player that are playing
	 * @return The list
	 */
	public List<Player> getPlaying() {
		return new ArrayList<>(playing);
	}
	
	/**
	 * Return a copy of player that are insides
	 * Including @playing and @spectators
	 * @return The list
	 */
	public List<Player> getInsides() {
		return new ArrayList<>(insides);
	}
	
	/**
	 * Return a copy of player that are in spectators
	 * @return The list
	 */
	public List<Player> getSpectators() {
		return new ArrayList<>(spectators);
	}
	
	/**
	 * Return a copy of all the teams
	 * @return The list
	 */
	public List<Team> getTeams() {
		return new ArrayList<>(teams);
	}
	
	/**
	 * Method to get the team of a player
	 * @param p The player
	 * @return The team where he is
	 */
	public Team getTeam(Player p) {
		for (Team t : teams)
			if (t.getPlayers().contains(p))
				return t;
		return null; // Should not happen
	}
	
	/**
	 * Add a team to the game
	 * @param t The team
	 */
	public void addTeam(Team t) {
		teams.add(t);
	}
	
	/**
	 * @return Number of team alive
	 */
	public int getTeamsAlive() {
		int count = 0;
		for (Team t : this.getTeams()) {
			if (t.getPlayerAlive() > 0)
				count++;
		}
		return count;
	}
	
	/**
	 * Remove a team to the game
	 * @param t The team
	 */
	public void removeTeam(Team t) {
		teams.remove(t);
	}
	
	/**
	 * Set nametag when no-gui mode or team disabled
	 * to display name in red and green
	 * Called on {@link Game#start()}
	 */
	public void setNameTag() {
		for (Team t : this.getTeams()) {
			for (Player p : t.getPlayers())
				setNameTag(p);
		}
	}
	
	/**
	 * Init nametag color that a player see
	 * @param p The player
	 */
	public void setNameTag(Player p) {
		Team t = getTeam(p);
		Game game = t.getGame();
		
		String randomNameGreen = Util.generateRandomString();
		String randomNameRed = Util.generateRandomString();
		
		NametagHandler handler = (NametagHandler) NMSHandler.getModule("NametagEdit");
		
		handler.init(randomNameGreen, randomNameGreen, NameTagColor.GREEN);
		
		for (Player pl : game.getPlaying())
			if (t.getPlayers().contains(pl))
				handler.addPlayer(pl);
		
		handler.sendToPlayer(p);
		
		handler.init(randomNameRed, randomNameRed, NameTagColor.RED);
		
		for (Player pl : game.getPlaying())
			if (!t.getPlayers().contains(pl))
				handler.addPlayer(pl);
		handler.sendToPlayer(p);
	}
	
	/**
	 * Is used in OldSpectatorHandler
	 * @param p The player
	 */
	public void setNameTagSpectator(Player p) {
		Game game = GameManager.getPlayerGame(p);
		
		String random = Util.generateRandomString();
		
		NametagHandler handler = (NametagHandler) NMSHandler.getModule("NametagEdit");
		
		handler.init(random, random, NameTagColor.GRAY);
		
		for (Player pl : game.getInsides())
				handler.addPlayer(pl);
		handler.sendToPlayer(p);
	}
	
	/**
	 * Call to fill the teams of a game
	 * System: If fill is enabled or gui mode enabled, we fill otherwise player that doesn't have a team we put them in a team
	 */
	public void fillTeam() {
		for (Player p : this.getPlaying()) {
			if (this.getTeam(p) == null) {
				if (!this.isFFA() && (this.isAutoFillTeam() || this.getTeamJoiningMode() == TeamJoiningMode.GUI) && teams.size() > 0) {
					joinMinTeam(p);
				} else {
					Team t = new Team(this, p);
					t.join(p);
					this.addTeam(t);
				}
			}
		}
	}
	
	/**
	 * Make a player join the team with less players
	 * @param p The player
	 */
	private void joinMinTeam(Player p) {
		List<Team> list = this.getTeams();
		list.sort(teamComparator);
		Team team = list.get(0);
		if (team.getPlayers().size() >= this.getTeamSize()) {
			Team t = new Team(this, p);
			t.join(p);
			this.addTeam(t);
		} else
			team.join(p);
	}
	
	/**
	 * Comparator to sort the teams
	 */
	private final Comparator<Team> teamComparator = Comparator.comparingInt(t -> t.getPlayers().size());
	
	/**
	 * Get the name of the game
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the world of the game
	 * @return world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Set the lobby of the game
	 * @param lobby The location lobby
	 */
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	/**
	 * @return Lobby of the game
	 */
	public Location getLobby() {
		return lobby;
	}

	/**
	 * Get number of free slot in the game!
	 * @return The number of free slot!
	 */
	public int getFreeSlot() {
		return this.maxPlayers - this.insides.size();
	}
	
	/**
	 * Used to compare two game object!
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Game)) return false;
		
		Game other = (Game) o;
		
		return other.name.equals(this.name);
	}
	
	
}
