package net.upd4ting.gameapi.util;

import org.bukkit.inventory.ItemStack;

public class NBTItem {

	private ItemStack bukkititem;
	
	public NBTItem(ItemStack Item){
		bukkititem = Item.clone();
	}
	
	public ItemStack getItem(){
		return bukkititem;
	}
	
	public NBTItem setString(String Key, String Text){
		bukkititem = NBTReflectionUtils.setString(bukkititem, Key, Text);
		return this;
	}
	
	public String getString(String Key){
		return NBTReflectionUtils.getString(bukkititem, Key);
	}
	
	public NBTItem setInteger(String key, Integer Int){
		bukkititem = NBTReflectionUtils.setInt(bukkititem, key, Int);
		return this;
	}
	
	public Integer getInteger(String key){
		return NBTReflectionUtils.getInt(bukkititem, key);
	}
	
	public NBTItem setDouble(String key, Double d){
		bukkititem = NBTReflectionUtils.setDouble(bukkititem, key, d);
		return this;
	}
	
	public Double getDouble(String key){
		return NBTReflectionUtils.getDouble(bukkititem, key);
	}
	
	public NBTItem setBoolean(String key, Boolean b){
		bukkititem = NBTReflectionUtils.setBoolean(bukkititem, key, b);
		return this;
	}
	
	public Boolean getBoolean(String key){
		return NBTReflectionUtils.getBoolean(bukkititem, key);
	}
	
	public Boolean hasKey(String key){
		return NBTReflectionUtils.hasKey(bukkititem, key);
	}
	
}