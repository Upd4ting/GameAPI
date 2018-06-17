package net.upd4ting.gameapi.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

class SpecialItemPlayerInteractListener implements Listener {

	// Ignore cancel = false sinon l'listener ignore les click dans le vide
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		SpecialItem item = SpecialItem.getSpecialItem(player.getItemInHand());
		if (item == null) return;
		// Click droit
		if (isRightClick(event.getAction()) && item.isRightClickable()) item.rightClickEvent(event);
		// Click gauche
		else if (isLeftClick(event.getAction()) && item.isLeftClickable()) item.leftClickEvent(event);
		else return;
		player.updateInventory();
	}

	public boolean isRightClick(Action action) {
		return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
	}

	public boolean isLeftClick(Action action) {
		return action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
	}
}
