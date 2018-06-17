package net.upd4ting.gameapi.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SpecialItemConsumable {
	void consumeItemEvent(Player player, ItemStack item);
}
