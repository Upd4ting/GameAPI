package net.upd4ting.gameapi.command.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.GamePlayer;
import net.upd4ting.gameapi.TeamJoiningMode;
import net.upd4ting.gameapi.command.Command;
import net.upd4ting.gameapi.team.Team;

public class TeamCommand extends Command {

	public TeamCommand() {
		super("team", null, false);
	}

	@Override
	public void constructCommands() {
		this.addAlias("teams", "team");
		this.addAlias("teammate", "team");
		this.addAlias("teammates", "team");
		
		this.addCommandAction("", (sender, args) -> {
            Player p = (Player) sender;
            Game game = GameManager.getPlayerGame(p);

            if (game == null) // The guys is not in a game
                return true;

            if (game.isFFA()) // Team not enabled
                return true;

            if (game.getTeamJoiningMode() != TeamJoiningMode.COMMAND) { // Team command not enabled
                game.sendConfigMessage(p, GameAPI.getLangConfiguration().getTeamCommandErrorGuiEnabled());
                return true;
            }

            if (!game.isWaiting()) { // Team command disabled ingame
                game.sendConfigMessage(p, GameAPI.getLangConfiguration().getTeamCommandErrorIngame());
                return true;
            }

            for (String s : GameAPI.getLangConfiguration().getTeamCommandHelpMessage()) {
                game.sendConfigMessage(p, s);
            }

            return true;
        });
		
		this.addCommandAction("%word", (sender, args) -> {
            Player p = (Player) sender;
            Game game = GameManager.getPlayerGame(p);
            String arg = args.get(0).getAsString();

            if (game == null) // The guys is not in a game
                return true;

            if (game.isFFA()) // Team not enabled
                return true;

            if (game.getTeamJoiningMode() != TeamJoiningMode.COMMAND) { // Team command not enabled
                game.sendConfigMessage(p, GameAPI.getLangConfiguration().getTeamCommandErrorGuiEnabled());
                return true;
            }

            if (!game.isWaiting()) { // Team command disabled ingame
                game.sendConfigMessage(p, GameAPI.getLangConfiguration().getTeamCommandErrorIngame());
                return true;
            }

            if (arg.equals("leave")) { // Player want to leave
                leaveTeam(game, p, true);
                return true;
            }

            Player invited = Bukkit.getPlayer(arg);

            // Not online
            if (invited == null || !invited.isOnline()) {
                game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorNotOnline(arg));
                return true;
            }

            GamePlayer up = GamePlayer.instanceOf(p);
            final GamePlayer upi = GamePlayer.instanceOf(invited);

            // Accept the invitation
            if (up.hasInvitation(invited)) {
                // Is the guys who invited the other got already a team?
                Team t;

                if (game.getTeam(invited) != null)
                    t = game.getTeam(invited);
                else {
                    t = new Team(game, invited);
                    t.join(invited);
                    game.addTeam(t);
                }

                // We make him leave his other team (if he got one)
                leaveTeam(game, p, false);

                // We make him join the new team
                t.join(p);

                // Send a message to everyone
                for (Player pl : t.getPlayers()) {
                    game.sendConfigMessage(pl, GameAPI.getLangConfiguration().getTeamCommandJoin(p));
                }

                // We accept the invitation
                up.acceptInvitation(invited);

                return true;
            }

            // Check if owner
            Team current = game.getTeam(p);
            if (current != null && !current.getOwner().getName().equals(p.getName())) {
                game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorNotOwner());
                return true;
            }

            // Already in the team
            if (current != null && current.getPlayers().contains(invited)) {
                game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorAlreadyInTeam(invited));
                return true;
            }

            // Already invited
            if (upi.hasInvitation(p)) {
                game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorAlreadyInvited(invited));
                return true;
            }

            Integer curSize = game.getTeam(p) != null ? game.getTeam(p).getPlayers().size() : 0;

            // Team Full
            if (curSize >= game.getTeamSize()) {
                game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorTeamFull());
                return true;
            }

            up.sendInvitation(invited);

            game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandSendInvitation(invited, game.getExpireTime()));
            game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), invited, GameAPI.getLangConfiguration().getTeamCommandReceiveInvitation(p, game.getExpireTime()));

            // Task to delete the invitation
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (upi.hasInvitation(p))
                        upi.acceptInvitation(p);
                }
            }.runTaskLater(GameAPI.getInstance(), game.getExpireTime() * 20);

            return true;
        });
	}
	
	private void leaveTeam(Game game, Player p, Boolean messageIfNotTeam) {
		final Team current = game.getTeam(p);
		
		// If not in team 
		if (current == null) {
			if (messageIfNotTeam)
				game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), p, GameAPI.getLangConfiguration().getTeamCommandErrorNotInTeam());
		}
		else { // We make him leave his team and we change the owner if he was the owner
			
			// Make him leave
			current.leave(p);
			
			// Message to all the players of the team
			for (Player pl : current.getPlayers()) {
				game.sendConfigMessage(pl, GameAPI.getLangConfiguration().getLeaveMessage(p));
			}
			
			if (current.getPlayers().size() == 0) {
				game.removeTeam(current);
			}
			else if (current.getOwner().equals(p)) {
				// New owner
				Player nOwner = current.getPlayers().get(0);
				current.setOwner(nOwner);
				
				// We send a message to all the players of the team
				for (Player pl : current.getPlayers()) {
					game.sendConfigMessage(GameAPI.getLangConfiguration().getTeamCommandPrefix(), pl, GameAPI.getLangConfiguration().getTeamCommandNewOwner(nOwner));
				}
			}
		}
	}

	@Override
	public List<String> getHelpMessage(CommandSender sender) {
		return new ArrayList<>();
	}
}
