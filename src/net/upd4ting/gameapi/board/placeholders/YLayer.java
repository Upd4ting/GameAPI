package net.upd4ting.gameapi.board.placeholders;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameState;
import net.upd4ting.gameapi.board.PlaceHolder;
import net.upd4ting.gameapi.board.Sideline;

public class YLayer extends PlaceHolder {

	public YLayer() {
		super("%ylayer", GameState.WAITING, GameState.FINISH, GameState.INGAME);
	}

	@Override
	public String process(Game game, Player p, Sideline sl, String conserned) {
		return conserned.replace(this.getName(), ""+ (int) p.getLocation().getY());
	}

}
