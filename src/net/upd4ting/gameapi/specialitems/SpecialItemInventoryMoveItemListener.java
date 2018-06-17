package net.upd4ting.gameapi.specialitems;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

class SpecialItemInventoryMoveItemListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		SpecialItem item = SpecialItem.getSpecialItem(event.getItem());
		if (item == null) return;
		Inventory init = event.getInitiator();
		Inventory dest = event.getDestination();
		// Movable
		if (!item.isMovable()) event.setCancelled(true);
		// Droppable
		else if (!item.isDroppable()) {
			// Sortir l'objet de l'inventaire joueur vers autre inventaire
			if (init.getType() == InventoryType.PLAYER && dest.getType() != InventoryType.PLAYER) {
				event.setCancelled(true);
			}
		}

	}
}
