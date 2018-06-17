package net.upd4ting.gameapi.inventory;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.upd4ting.gameapi.inventory.InventoryItem.ActionItem;

public class InventoryListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){	
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() == null)
			return;
		
		Inventory current = null;

		if (Inventory.currentInventory.containsKey(p.getUniqueId())){
			current = Inventory.currentInventory.get(p.getUniqueId());
		}
		
		if (e.getSlot() == -1 || e.getCurrentItem() == null) 
			return;
		
		if (current != null && e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
			if (current.getItems()[e.getSlot()] != null){
				ActionItem action = current.getItems()[e.getSlot()].getAction();
				if (action != null) {
					action.run(e);
				}
			}

			e.setCancelled(true);
		}
	}

	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();

		if (Inventory.currentInventory.containsKey(uuid)) {
			Inventory.currentInventory.remove(uuid);
		}
	}
}
