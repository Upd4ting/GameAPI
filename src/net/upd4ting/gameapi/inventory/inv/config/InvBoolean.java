package net.upd4ting.gameapi.inventory.inv.config;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.inventory.InventoryItem;
import net.upd4ting.gameapi.util.CC;
import net.upd4ting.gameapi.util.UtilItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InvBoolean extends InvConfig {

    public InvBoolean(Player player, Game game, String field) {
        super(game, field, GameAPI.getLangConfiguration().getGameConfigInventoryName(), 9, player, true);
    }

    @Override
    public void init() {
        this.generateReturn(new InvListConfig(player, game), 0, UtilItem.create(GameAPI.getLangConfiguration().getItemReturnName(), Material.ARROW));

        Boolean value = (Boolean) this.getObject();

        this.addItem(4, new InventoryItem(UtilItem.create(CC.aqua + field + ": " + CC.gold + value, Material.STAINED_CLAY, (byte) (value ? 13 : 14)),event -> this.setObject(!value)));
    }
}
