package net.upd4ting.gameapi.inventory.inv;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.inventory.Inventory;
import net.upd4ting.gameapi.inventory.InventoryItem;
import net.upd4ting.gameapi.schematic.Schematic.BlockInfo;
import net.upd4ting.gameapi.team.Team;
import net.upd4ting.gameapi.util.UtilItem;

public class InvTeam extends Inventory {
	
	final Game game;
	
	public InvTeam(Player player, Game game) {
		super(GameAPI.getLangConfiguration().getTeamInventoryName(), 9 * ((game.getTeams().size() / 9)+1), player, true);
		this.game = game;
	}

	@Override
	public void init() {
		Integer index = 0;
		final Integer sizeMax = game.getTeamSize();
		
		for (final Team t : game.getTeams()) {
			ArrayList<String> lores = new ArrayList<>();
			for (Player p : t.getPlayers()) lores.add(ChatColor.WHITE + p.getName());
			
			BlockInfo info = t.getBlock();
			String infoSize = ChatColor.GRAY + " [" + ChatColor.GREEN + t.getPlayers().size() + 
					ChatColor.DARK_GRAY + "/" + ChatColor.RED + sizeMax + ChatColor.GRAY + "]";
					
			addItem(index, new InventoryItem(UtilItem.create(t.getName() + infoSize, info.material, 
					info.data, lores), event -> {
                        if (t.getPlayers().contains(player) || t.getPlayers().size() >= sizeMax)
                            return;
                        Team ancient = game.getTeam(player);
                        if (ancient != null) {
                            ancient.leave(player);

                            // Message to all the players of the team
                            for (Player pl : ancient.getPlayers()) {
                                game.sendConfigMessage(pl, GameAPI.getLangConfiguration().getLeaveMessage(player));
                            }
                        }

                        t.join(player);

                        // Send a message to everyone
                        for (Player pl : t.getPlayers()) {
                            game.sendConfigMessage(pl, GameAPI.getLangConfiguration().getTeamCommandJoin(player));
                        }
                    }));
			
			index++;
		}
	}

}
