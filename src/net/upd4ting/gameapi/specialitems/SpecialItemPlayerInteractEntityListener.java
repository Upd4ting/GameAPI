package net.upd4ting.gameapi.specialitems;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

class SpecialItemPlayerInteractEntityListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		SpecialItem item = SpecialItem.getSpecialItem(player.getItemInHand());
		if (item != null && !item.isDroppable() && entity instanceof ItemFrame)
			event.setCancelled(true);
	}
}
