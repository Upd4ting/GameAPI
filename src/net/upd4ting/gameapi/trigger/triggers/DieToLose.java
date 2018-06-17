package net.upd4ting.gameapi.trigger.triggers;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.trigger.Trigger;

public class DieToLose implements Trigger {

	@Override
	public boolean onDie(Game game, Player p) {
		return true;
	}

}
