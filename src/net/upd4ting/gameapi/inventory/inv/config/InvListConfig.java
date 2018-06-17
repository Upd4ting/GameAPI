package net.upd4ting.gameapi.inventory.inv.config;

import net.upd4ting.gameapi.CustomInventoryConfig;
import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.InventoryType;
import net.upd4ting.gameapi.configuration.Configuration;
import net.upd4ting.gameapi.inventory.Inventory;
import net.upd4ting.gameapi.inventory.InventoryItem;
import net.upd4ting.gameapi.util.CC;
import net.upd4ting.gameapi.util.Reflector;
import net.upd4ting.gameapi.util.Util;
import net.upd4ting.gameapi.util.UtilItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Inventory that display all Changeable field
 * of a game.
 */
public class InvListConfig extends Inventory {

    private final Game game;

    public InvListConfig(Player player, Game game) {
        super(GameAPI.getLangConfiguration().getGameConfigInventoryName(), 63, player, false);
        this.game = game;
    }

    @Override
    public void init() {
        int index = 0;

        List<Field> fields = new ArrayList<>();
        Reflector.getAllFields(fields, game.getClass());

        // We sort the field by alphabetic order
        Collections.sort(fields, ALPHABETICAL_ORDER);

        for (Field field : fields) {

            if (!field.isAnnotationPresent(Game.Changeable.class))
                continue;

            Game.Changeable c = field.getAnnotation(Game.Changeable.class);

            Configuration config = Configuration.getConfiguration(c.config());

            List<String> lores = new ArrayList<>();

            for (String s : config.getStringListConfig("description." +field.getName())) {
                lores.add(CC.gray + s);
            }

            // Build icon
            boolean set = false;

            try {
                field.setAccessible(true);
                set = field.get(game) != null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            ItemStack icon = UtilItem.create(config.getStringConfig("name."+field.getName()), Material.STAINED_CLAY, set ? (byte) 5 : (byte) 14);

            addItem(index++, new InventoryItem(icon, event -> {
                Class<?> clazz = field.getType();
                InventoryType.InventoryBuilder builder = InventoryType.getInventoryForType(clazz);

                if (builder == null) {
                    Util.log("Configuration type: " + clazz.getName() + " not supported... Report it.");
                    return;
                }

                builder.createInvConfig(player, game, field.getName()).open();
            }));
        }

        // Then we add custom config :)
        for (CustomInventoryConfig cic : CustomInventoryConfig.getCustomConfigs()) {
            ItemStack icon = UtilItem.create(cic.getName(), Material.STAINED_CLAY, cic.getBuilder().isSet() ? (byte) 5 : (byte) 14);
            addItem(index++, new InventoryItem(icon, event -> {
                cic.getBuilder().build(player).open();
            }));
        }
    }

    private static Comparator<Field> ALPHABETICAL_ORDER = (f1, f2) -> {
        int res = String.CASE_INSENSITIVE_ORDER.compare(f1.getName(), f2.getName());
        if (res == 0) {
            res = f1.getName().compareTo(f2.getName());
        }
        return res;
    };
}
