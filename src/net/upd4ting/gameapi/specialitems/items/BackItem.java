package net.upd4ting.gameapi.specialitems.items;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.specialitems.SpecialItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class BackItem extends SpecialItem {

    public BackItem() {
        super(GameAPI.getGameConfiguration().getHubItemName(), new ItemStack(Material.getMaterial(GameAPI.getGameConfiguration().getHubItemId()), 1, (byte) GameAPI.getGameConfiguration().getHubItemData()));
        this.setDroppable(false);
        this.setMovable(false);
        this.setInventoryClickable(true);
        this.setLeftClickable(false);
        this.setRightClickable(true);
    }

    @Override
    public void inventoryClickEvent(Player player) {
        Game game = GameManager.getPlayerGame(player);

        if (game == null)
            return;

        game.expulse(player, null);
    }

    @Override
    public void rightClickEvent(Player player) {
        Game game = GameManager.getPlayerGame(player);

        if (game == null)
            return;

        game.expulse(player, null);
    }
}
