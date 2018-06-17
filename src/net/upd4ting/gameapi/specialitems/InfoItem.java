package net.upd4ting.gameapi.specialitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Un Item qui ne contient que des informations
 * @author Likaos
 */
@SuppressWarnings("deprecation")
public abstract class InfoItem extends SpecialItem {

	public InfoItem(String name, String[] lore, ItemStack itemStack) {
		super(name, lore, itemStack);
		this.setInventoryClickable(true);
	}

	@Override
	public void rightClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void leftClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	@Override
	public void middleClickEvent(Player player) {
		inventoryClickEvent(player);
	}

	/**
	 * La méthode qui sera appelée quand l'inventaire virtuel recupère l'objet
	 * @param player The player
	 * @return The item
	 */
	public abstract ItemStack getInfoItem(Player player);

}
