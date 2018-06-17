package net.upd4ting.gameapi.board.placeholders;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameState;
import net.upd4ting.gameapi.board.PlaceHolder;
import net.upd4ting.gameapi.board.Sideline;
import net.upd4ting.gameapi.stats.Stat;

public class KillGame extends PlaceHolder {

	public KillGame() {
		super("%gameKill", GameState.INGAME, GameState.FINISH);
	}

	@Override
	public String process(Game game, Player p, final Sideline sl, String conserned) {
		return conserned.replace(this.getName(), ""+  Stat.KILLGAME.get(p));
	}

}
