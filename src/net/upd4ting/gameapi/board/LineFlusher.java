package net.upd4ting.gameapi.board;

import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;

/**
 * Class to flush line taking count of placeholder
 * @author Upd4ting
 *
 */
public final class LineFlusher {
	
	private final Sideline line;
	
	private LineFlusher(Sideline line) { // Non instanciable class
		this.line = line;
	}
	
	public void flushLine(Game game, Player p, String lineToFlush) {
		PlaceHolder.execute(game, p, line, lineToFlush);
	}
	
	public static LineFlusher newInstance(Sideline line) {
		return new LineFlusher(line);
	}
}
