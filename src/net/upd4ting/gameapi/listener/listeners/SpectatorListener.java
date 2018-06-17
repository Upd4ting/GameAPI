package net.upd4ting.gameapi.listener.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.team.Team;

/**
 * Listener that will ensure that spectator stay near their allies
 * @author Upd4ting
 *
 */
public class SpectatorListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Location to = e.getTo();
		Location from = e.getFrom();
		
		// Optimization because PlayerMoveEvent is called a lot!
		if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())
			return;
		
		Player p = e.getPlayer();
		Game game = GameManager.getPlayerGame(p);
		
		if (game == null)
			return;
		
		if (game.isInGame() && game.getSpectators().contains(p)) {
			Team team = game.getTeam(p);
			
			// INFO: Team can be equals to null because if spectatorjoin is enabled in the game
			// when he joins he got no team... its why we check if team equals to null or not
			
			Double minDistance = -1d;
			Player nearest = null;
			
			for (Player player : game.getPlaying()) {
				if (game.isSpectatorOnlyMate() && team != null && !team.getPlayers().contains(player))
					continue;
				if (!player.getWorld().getName().equals(p.getWorld().getName()))
					continue;
				
				Double d = player.getLocation().distanceSquared(p.getLocation());
				if (minDistance == -1 || d < minDistance) {
					minDistance = d;
					nearest = player;
				}
			}
			
			int dist = game.getSpectatorDistance();
			dist *= dist;
			
			if (minDistance > dist) {
				p.teleport(nearest.getLocation().add(0,2,0));
				game.sendConfigMessage(p, GameAPI.getLangConfiguration().getSpectatorTp());
			}
		}
	}
}
