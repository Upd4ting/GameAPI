package net.upd4ting.gameapi.specialitems;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;


class SpecialItemPlayerDropItemListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		SpecialItem item = SpecialItem.getSpecialItem(event.getItemDrop().getItemStack());
		if (item == null) return;
		if (!item.isDroppable()) {
			event.setCancelled(true);
		}
		item.dropEvent(event);
	}
}
