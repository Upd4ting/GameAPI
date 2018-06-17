package net.upd4ting.gameapi.trigger;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;

public interface Trigger {
	/**
	 * @return If the player lose or not
	 */
    boolean onDie(Game game, Player p);
}
