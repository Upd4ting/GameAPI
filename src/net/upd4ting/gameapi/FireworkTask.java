package net.upd4ting.gameapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.upd4ting.gameapi.util.UtilFirework;

public class FireworkTask extends BukkitRunnable {
	
	private final List<Player> players = new ArrayList<>();
	private int i = 0;
	
	public FireworkTask(Player player) {
		this.players.add(player);
	}
	
	public FireworkTask(List<Player> players) {
		this.players.addAll(players);
	}
	
	
	@Override
	public void run() {
		for (Player p : players) {
			if (!p.isOnline())
				continue;
			
			UtilFirework.spawnRandomFirework(p.getLocation().add(0,1.5,0), false);
		}

		i++;

		if (i > 10) {
			this.cancel();
		}
	}
	
}
