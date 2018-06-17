package net.upd4ting.gameapi.signs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.collect.Lists;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.configuration.configs.LangConfig;

/**
 * Class to handle sign
 * @author Upd4ting
 *
 */
public final class Sign {
	
	private static String PREFIX = "[GAME]";
	private static final List<Sign> signs = Lists.newArrayList();
	
	private final Location loc;
	private final Game game;
	
	private Sign(Location loc, Game game) { // Non instanciable class
		this.loc = loc;
		this.game = game;
	}
	
	public void update() {		
		LangConfig mHandler = GameAPI.getLangConfiguration();
		String motd;
		
		if (game.isWaiting())
			motd = mHandler.getMotdWaiting();
		else if (game.isInGame())
			motd = mHandler.getMotdIngame();
		else
			motd = mHandler.getMotdFinish();
		
		String[] lines = mHandler.getSignFormat(mHandler.getPrefix(), motd, game.getName(), game.getInsides().size(), game.getMaxPlayers());
		
		org.bukkit.block.Sign sign = (org.bukkit.block.Sign) loc.getBlock().getState();
		for (int i = 0; i < 4; i++)
			sign.setLine(i, lines[i]);
		
		sign.update(true);
	}

	public Location getLoc() {
		return loc;
	}

	public Game getGame() {
		return game;
	}
	
	public static Sign newInstance(Location loc, String name) {
		Game game = GameManager.getGame(name);
		
		if (game == null)
			return null;
		
		Sign sign = new Sign(loc, game);
		signs.add(sign);
		
		return sign;
	}
	
	public static void init() {
		GameAPI.getInstance().getServer().getPluginManager().registerEvents(new SignListener(), GameAPI.getInstance());
		SignTask.getInstance().start();
	}
	
	public static void setPrefix(String prefix) {
		PREFIX = prefix;
	}
	
	public static String getPrefix() {
		return PREFIX;
	}
	
	public static List<Sign> getSigns() {
		return new ArrayList<>(signs);
	}

	public static Sign getSign(Location loc) {
		for (Sign sign : signs)
			if (sign.getLoc().distanceSquared(loc) < 1)
				return sign;
		return null;
	}

	public static void removeSign(Sign sign) {
		signs.remove(sign);
	}

	public static void loadAll() {
		File folder = new File(GameAPI.getInstance().getDataFolder(), "signs");

		folder.mkdirs();

		if (folder.listFiles() == null)
			return;

		for (File f : folder.listFiles()) {
			String name = f.getName();
			String gameName = name.split("###")[0];
			String[] loc = name.split("###")[1].split("\\$");

			Location l = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));

			Sign.newInstance(l, gameName);

			f.delete();
		}
	}

	public static void saveAll() {
		File folder = new File(GameAPI.getInstance().getDataFolder(), "signs");

		for (Sign sign : Sign.getSigns()) {
			Location loc = sign.getLoc();
			File file = new File(folder, sign.getGame().getName() + "###" + loc.getWorld().getName() + "$" + loc.getBlockX() + "$" + loc.getBlockY() + "$" + loc.getBlockZ());

			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
