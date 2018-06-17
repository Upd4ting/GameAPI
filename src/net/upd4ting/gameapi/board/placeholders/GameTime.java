package net.upd4ting.gameapi.board.placeholders;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameState;
import net.upd4ting.gameapi.board.PlaceHolder;
import net.upd4ting.gameapi.board.Sideline;
import net.upd4ting.gameapi.util.Util;

public class GameTime extends PlaceHolder {

	public GameTime() {
		super("%gametime", GameState.WAITING, GameState.INGAME, GameState.FINISH);
	}

	@Override
	public String process(Game game, Player p, Sideline sl, String conserned) {
		return conserned.replace(this.getName(), Util.getDisplayTime(game.getTime()));
	}

}
