package net.upd4ting.gameapi.inventory.inv.config;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.inventory.InventoryItem;
import net.upd4ting.gameapi.util.CC;
import net.upd4ting.gameapi.util.UtilItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InvDouble extends InvConfig {

    public InvDouble(Player player, Game game, String field) {
        super(game, field, GameAPI.getLangConfiguration().getGameConfigInventoryName(), 18, player, true);
    }

    @Override
    public void init() {
        this.generateReturn(new InvListConfig(player, game), 0, UtilItem.create(GameAPI.getLangConfiguration().getItemReturnName(), Material.ARROW));

        Double number = (Double) this.getObject();

        this.addItem(1, new InventoryItem(UtilItem.create(CC.aqua + "-10", Material.STONE_BUTTON), (event -> this.setObject(number - 10))));

        this.addItem(2, new InventoryItem(UtilItem.create(CC.aqua + "-5", Material.STONE_BUTTON), (event -> this.setObject(number - 5))));

        this.addItem(3, new InventoryItem(UtilItem.create(CC.aqua + "-1", Material.STONE_BUTTON), (event -> this.setObject(number - 1))));

        this.addItem(4, new InventoryItem(UtilItem.create(CC.aqua + field + ": " + CC.gold + number, Material.ANVIL), (event -> {

        })));

        this.addItem(5, new InventoryItem(UtilItem.create(CC.aqua + "+1", Material.STONE_BUTTON), (event -> this.setObject(number + 1))));

        this.addItem(6, new InventoryItem(UtilItem.create(CC.aqua + "+5", Material.STONE_BUTTON), (event -> this.setObject(number + 5))));

        this.addItem(7, new InventoryItem(UtilItem.create(CC.aqua + "+10", Material.STONE_BUTTON), (event -> this.setObject(number + 10))));

        this.addItem(11, new InventoryItem(UtilItem.create(CC.aqua + "-0.5", Material.STONE_BUTTON), (event -> this.setObject(number - 0.5))));

        this.addItem(12, new InventoryItem(UtilItem.create(CC.aqua + "-0.1", Material.STONE_BUTTON), (event -> this.setObject(number - 0.1))));

        this.addItem(14, new InventoryItem(UtilItem.create(CC.aqua + "+0.1", Material.STONE_BUTTON), (event -> this.setObject(number + 0.1))));

        this.addItem(15, new InventoryItem(UtilItem.create(CC.aqua + "+0.5", Material.STONE_BUTTON), (event -> this.setObject(number + 0.5))));
    }
}
