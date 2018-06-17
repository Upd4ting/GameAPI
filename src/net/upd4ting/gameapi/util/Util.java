package net.upd4ting.gameapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * Some utils function
 * @author Upd4ting
 *
 */
public class Util {
	
	/**
	 * Generate the chunks around a Location
	 * @param loc Location to load
	 * @param radius Radius arount the location
	 * @param load Do we load the chunk or not
	 * @return The list of Chunk that has been loaded
	 */
	public static List<Chunk> loadChunks(Location loc, Integer radius, Boolean load) {
		List<Chunk> chunks = new ArrayList<>();
		
		int cx = loc.getBlockX() - radius;
		int cz = loc.getBlockZ() - radius;
		
		while (cx <= loc.getBlockX() + radius) {
			while (cz <= loc.getBlockZ() + radius) {
				Location l = new Location(loc.getWorld(), cx, 0, cz);
				if (load && !l.getChunk().isLoaded())
					l.getWorld().loadChunk(l.getChunk().getX(), l.getChunk().getZ(), true);
				chunks.add(l.getChunk());
				cz += 16;
			}
			cz = loc.getBlockZ() - radius;
			cx += 16;
		}
		
		return chunks;
	}
	
	/**
	 * @param time The time in second
	 * @return Get display time
	 */
	public static String getDisplayTime(int time) { 
		return String.format("%02d:%02d", time / 60, time % 60); 
	}
	
	/**
	 * Generate a random string
	 * @return A random string filled with letter a-z
	 */
	public static String generateRandomString() {
		char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
				'n', 'o', 'p', 'q', 'r', 's', 'u', 'v', 'w', 'x', 'y', 'z' };
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		
		for (int i = 0; i < 10; i++) {
			int index = r.nextInt(25);
			sb.append(letters[index]);
		}
		
		return sb.toString();
	}
	
	/**
	 * Send a log in the console
	 * @param message The message
	 */
    public static void log(String message) {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[GameAPI by Upd4ting] " + ChatColor.RESET + message);
    }

	/**
	 * Get Highest location
	 * @param loc The location
	 * @return The highest location
	 */
	public static Location getHighestLoc(Location loc) {
    	loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
    	return loc;
	}
}
