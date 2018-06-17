package net.upd4ting.gameapi.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UtilItem {

	public static ItemStack create(String name, Material mat, byte data) {
		ItemStack is = new ItemStack(mat, 1, data);

		ItemMeta isM = is.getItemMeta();
		isM.setDisplayName(name);
		is.setItemMeta(isM);

		return is;
	}

	public static ItemStack create(String name, Material mat, byte data, List<String> lores) {
		ItemStack is = new ItemStack(mat, 1, data);

		ItemMeta isM = is.getItemMeta();
		if (name != null)
			isM.setDisplayName(name);

		if (lores != null && lores.size() > 0)
			isM.setLore(lores);

		is.setItemMeta(isM);

		return is;
	}

	public static ItemStack create(String name, Material mat, String ...lores) {
		return create(name, mat, (byte) 0, Arrays.asList(lores));
	}

	public static ItemStack create(String name, Material mat, int count, String ...lores) {
		ItemStack stack =  create(name, mat, (byte) 0, Arrays.asList(lores));
		stack.setAmount(count);
		return stack;
	}

	public static ItemStack createLeatherArmor(Material mat, ChatColor color) {
		ItemStack stack = new ItemStack(mat, 1);
		LeatherArmorMeta lm = (LeatherArmorMeta) stack.getItemMeta();
		lm.setColor(translateChatColorToColor(color));
		stack.setItemMeta(lm);
		return stack;
	}

	public static Color translateChatColorToColor(ChatColor chatColor) {
		switch (chatColor) {
			case AQUA:
				return Color.AQUA;
			case BLACK:
				return Color.BLACK;
			case BLUE:
				return Color.BLUE;
			case DARK_AQUA:
				return Color.AQUA;
			case DARK_BLUE:
				return Color.BLUE;
			case DARK_GRAY:
				return Color.GRAY;
			case DARK_GREEN:
				return Color.GREEN;
			case DARK_PURPLE:
				return Color.PURPLE;
			case DARK_RED:
				return Color.RED;
			case GOLD:
				return Color.YELLOW;
			case GRAY:
				return Color.GRAY;
			case GREEN:
				return Color.GREEN;
			case LIGHT_PURPLE:
				return Color.PURPLE;
			case RED:
				return Color.RED;
			case WHITE:
				return Color.WHITE;
			case YELLOW:
				return Color.YELLOW;
			default:
				break;
		}

		return null;
	}

	public static DyeColor translateChatColorToDyeColor(ChatColor chatColor) {
		switch (chatColor) {
			case AQUA:
				return DyeColor.CYAN;
			case BLACK:
				return DyeColor.BLACK;
			case BLUE:
				return DyeColor.BLUE;
			case DARK_AQUA:
				return DyeColor.CYAN;
			case DARK_BLUE:
				return DyeColor.BLUE;
			case DARK_GRAY:
				return DyeColor.GRAY;
			case DARK_GREEN:
				return DyeColor.GREEN;
			case DARK_PURPLE:
				return DyeColor.PURPLE;
			case DARK_RED:
				return DyeColor.RED;
			case GOLD:
				return DyeColor.YELLOW;
			case GRAY:
				return DyeColor.GRAY;
			case GREEN:
				return DyeColor.GREEN;
			case LIGHT_PURPLE:
				return DyeColor.PURPLE;
			case RED:
				return DyeColor.RED;
			case WHITE:
				return DyeColor.WHITE;
			case YELLOW:
				return DyeColor.YELLOW;
			default:
				break;
		}

		return null;
	}

	public static boolean isArmor(Material mat) {
		return mat == Material.CHAINMAIL_BOOTS || mat == Material.CHAINMAIL_CHESTPLATE || mat == Material.CHAINMAIL_HELMET || mat == Material.CHAINMAIL_LEGGINGS
				|| mat == Material.IRON_BOOTS || mat == Material.IRON_CHESTPLATE || mat == Material.IRON_LEGGINGS || mat == Material.IRON_HELMET ||
				mat == Material.DIAMOND_HELMET || mat == Material.DIAMOND_CHESTPLATE || mat == Material.DIAMOND_LEGGINGS || mat == Material.DIAMOND_BOOTS;
	}

	public static boolean isSword(Material mat) {
		return mat == Material.WOOD_SWORD || mat == Material.GOLD_SWORD || mat == Material.IRON_SWORD || mat == Material.DIAMOND_SWORD
				|| mat == Material.STONE_SWORD;
	}

	public static void makeGlow(ItemStack stack) {
		ItemMeta im = stack.getItemMeta();
		Glow glow = new Glow(999);
		im.addEnchant(glow, 1, true);
		stack.setItemMeta(im);
	}

	public static void registerGlow() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Glow glow = new Glow(70);
			Enchantment.registerEnchantment(glow);
		}
		catch (IllegalArgumentException e){
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
