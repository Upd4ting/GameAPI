package net.upd4ting.gameapi.gamehandler.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.gamehandler.SpectatorHandler;

/**
 * Class used to handle spectator
 * like on 1.7 minecraft version
 * @author Upd4ting
 *
 */
public class OldSpectatorHandler implements SpectatorHandler, Listener {

	@Override
	public void setSpectator(Game g, Player p) {
		p.setGameMode(GameMode.CREATIVE);
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.updateInventory();
		
		for (Player pl : g.getInsides())
			pl.hidePlayer(p);
	}

	@Override
	public void onJoin(Game g, Player p) {
		for (Player pl : g.getInsides()) {
			
			if (g.getSpectators().contains(p))
				pl.hidePlayer(p);
			if (g.getSpectators().contains(pl))
				p.hidePlayer(pl);
		}
	}

}
