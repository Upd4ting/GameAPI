package net.upd4ting.gameapi.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameManager;

/**
 * Class that will block all the action
 * that ingame player can't do when waiting or spectators
 * @author Upd4ting
 *
 */
public class Block implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Game game = GameManager.getPlayerGame(p);
		
		if (game == null) 
			return;
		
		// To prevent spectator to give items in chest or wathever for others players
		if (game.getSpectators().contains(p) && e.getClickedInventory() != null && e.getClickedInventory().getHolder() != null && !(e.getClickedInventory().getHolder() instanceof Player))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (GameManager.isLimited(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		if (GameManager.isLimited(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (GameManager.isLimited(e.getPlayer()) && !e.getPlayer().hasPermission("game.admin"))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (GameManager.isLimited(e.getPlayer()) && !e.getPlayer().hasPermission("game.admin"))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (GameManager.isLimited(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (GameManager.isLimited(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if (GameManager.isLimited((Player) e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (!GameManager.canSpawn(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		if (GameManager.isLimited(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) return;

		if (GameManager.isLimited((Player) e.getEntity())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (GameManager.isLimited((Player) e.getEntity()))
				e.setCancelled(true);
		}
		
		if (e.getDamager() instanceof Player) {
			if (GameManager.isLimited((Player) e.getDamager()))
				e.setCancelled(true);
		}
	}
}
