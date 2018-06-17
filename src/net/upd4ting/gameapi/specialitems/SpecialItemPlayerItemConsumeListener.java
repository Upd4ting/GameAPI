package net.upd4ting.gameapi.specialitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

class SpecialItemPlayerItemConsumeListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(itemStack == null || itemStack.getType().equals(Material.AIR)) return;
		SpecialItem specialItem = SpecialItem.getSpecialItem(itemStack);
		if(!(specialItem instanceof SpecialItemConsumable)) return;
		((SpecialItemConsumable)specialItem).consumeItemEvent(player, itemStack);
		event.setCancelled(true);
	}
}
