package net.upd4ting.gameapi.gamehandler.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.gamehandler.SpectatorHandler;

/**
 * Class used to handle spectator
 * like 1.8+ version minecraft
 * @author Upd4ting
 *
 */
public class NormalSpectatorHandler implements SpectatorHandler {

	@Override
	public void setSpectator(Game g, Player p) {
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.updateInventory();
		p.setGameMode(GameMode.SPECTATOR);
	}

	@Override
	public void onJoin(Game g, Player p) {}

}
