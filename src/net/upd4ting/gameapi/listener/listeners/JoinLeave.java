package net.upd4ting.gameapi.listener.listeners;

import net.upd4ting.gameapi.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * This class will listen to Join and Leave listener!
 * 
 * It handles:
 *  - Join the first game availaible if bungee mode enabled
 *  - Make a player rejoin the game he left
 * 
 * @author Upd4ting
 *
 */
public class JoinLeave implements Listener {
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		
		if (GameAPI.getGameConfiguration().isBungeeMode()) {
			Game game = GameManager.getBungeeGame();

			if (game == null)
				return;
			
			JoinResult result = game.canJoin(p);
			
			if (!result.hasJoined()) {
				String message = result.getMessage() == null ? "" : result.getMessage();
				e.setResult(Result.KICK_OTHER);
				e.setKickMessage(message);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if (GameAPI.getGameConfiguration().isBungeeMode()) {
			Game game = GameManager.getBungeeGame();

			if (game == null)
				return;

			game.join(p);

			e.setJoinMessage(null);
		} else {
			// If not bungee and scoreboard to true we need to reset his scoreboard cause health in tab stay
			// otherwise
			GamePlayer gp = GamePlayer.instanceOf(p);

			if (gp.isScoreboard()) {
				gp.setScoreboard(false);
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		// We don't care if we are in bungee mode or not, we must leave a game where we are
		Player p = e.getPlayer();
		
		Game game = GameManager.getPlayerGame(p);
		
		if (game != null) {
			// If multiarena, at next login we need to reset scoreboard
			GamePlayer gp = GamePlayer.instanceOf(p);
			gp.setScoreboard(true);

			game.expulse(p, null);
		}

		// Si bungee on désactive le message normal :)
		if (GameAPI.getGameConfiguration().isBungeeMode())
			e.setQuitMessage(null);
	}
}
