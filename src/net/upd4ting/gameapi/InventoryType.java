package net.upd4ting.gameapi;

import net.upd4ting.gameapi.inventory.inv.config.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Match an InvConfig inventory to a type of data
 * that can be changeable
 */
public abstract class InventoryType {
    private static HashMap<Class<?>, InventoryBuilder> inventories = new HashMap<>();

    public interface InventoryBuilder {
        InvConfig createInvConfig(Player player, Game game, String field);
    }

    public static boolean registerType(Class<?> clazz, InventoryBuilder builder) {
        if (inventories.containsKey(clazz))
            return false;

        inventories.put(clazz, builder);
        return true;
    }

    public static InventoryBuilder getInventoryForType(Class<?> clazz) {
        return inventories.get(clazz);
    }
}
