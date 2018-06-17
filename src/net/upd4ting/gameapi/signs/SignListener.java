package net.upd4ting.gameapi.signs;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.JoinResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class that listen to all sign listener
 * @author Upd4ting
 *
 */
public class SignListener implements Listener {
	
	@EventHandler
	public void onSignCreated(SignChangeEvent e) {
		Player p = e.getPlayer();
		
		if (!p.hasPermission("game.admin"))
			return;
		
		if (!e.getLine(0).equalsIgnoreCase(Sign.getPrefix()))
			return;
		
		String name = e.getLine(1);
		Location loc = e.getBlock().getLocation();
		
		Sign sign = Sign.newInstance(loc, name);
		
		if (sign == null) {
			p.sendMessage(ChatColor.RED + "Game name: " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
		} else {
			Sign.saveAll();
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();

		if (b.getType() != Material.SIGN && b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST)
			return;

		Sign sign = Sign.getSign(b.getLocation());

		if (sign == null)
			return;

		// If guys has no perm to create sign
		if (!p.hasPermission("game.admin")) {
			e.setCancelled(true);
			return;
		}

		Sign.removeSign(sign);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		// If ingame he can't use sign
		if (GameManager.getPlayerGame(p) != null)
			return;

		Block b = e.getClickedBlock();

		if (b == null || (b.getType() != Material.SIGN && b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST))
			return;

		Sign sign = Sign.getSign(b.getLocation());

		if (sign == null)
			return;

		e.setCancelled(true);

		Game game = sign.getGame();

		JoinResult result = game.canJoin(p);

		if (result.hasJoined())
			game.join(p);
		else
			p.sendMessage(result.getMessage());
	}
}
