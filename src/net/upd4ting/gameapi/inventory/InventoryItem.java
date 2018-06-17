package net.upd4ting.gameapi.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryItem extends ItemStack{
	private final ItemStack item;
	private final ActionItem action;
	private final Boolean refreshable;
	
	public InventoryItem(ItemStack item, ActionItem action){
		super(item);
		this.item = item;
		this.action = action;
		this.refreshable = true;
	}
	
	public InventoryItem(ItemStack item, Boolean refreshable, ActionItem action) {
		super(item);
		this.item = item;
		this.action = action;
		this.refreshable = refreshable;
	}
	
	
	public ItemStack getItem() {
		return item;
	}
	
	public ActionItem getAction() {
		return action;
	}
	
	public Boolean isRefreshable() {
		return refreshable;
	}
	
	public interface ActionItem{
		void run(InventoryClickEvent event);
	}
}
