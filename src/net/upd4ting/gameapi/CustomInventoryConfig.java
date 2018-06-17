package net.upd4ting.gameapi;

import net.upd4ting.gameapi.inventory.Inventory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomInventoryConfig {

    private static List<CustomInventoryConfig> customConfigs = new ArrayList<>();

    public interface CustomInventoryBuilder {
        Inventory build(Player player);
        boolean isSet();
    }

    private String name;
    private CustomInventoryBuilder builder;

    public CustomInventoryConfig(String name, CustomInventoryBuilder builder) {
        this.name = name;
        this.builder = builder;
    }

    public String getName() {
        return name;
    }

    public CustomInventoryBuilder getBuilder() {
        return builder;
    }

    public static void registerCustomConfig(CustomInventoryConfig cic) {
        customConfigs.add(cic);
    }

    public static List<CustomInventoryConfig> getCustomConfigs() {
        return customConfigs;
    }
}
