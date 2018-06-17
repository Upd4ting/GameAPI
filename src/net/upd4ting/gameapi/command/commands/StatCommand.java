package net.upd4ting.gameapi.command.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.GamePlayer;
import net.upd4ting.gameapi.command.Command;
import net.upd4ting.gameapi.stats.Stat;

public class StatCommand extends Command {

	public StatCommand() {
		super("stat", null, false);
	}

	@Override
	public void constructCommands() {
		this.addAlias("statistique", "stat");
		this.addAlias("stats", "stat");
		
		this.addCommandAction("%word", (sender, args) -> {
            Player p = (Player) sender;
            Game game = GameManager.getPlayerGame(p);

            if (game == null)
                return true;

            String player = args.get(0).getAsString();

            Player target = Bukkit.getPlayer(player);
            GamePlayer gp = GamePlayer.instanceOf(target);
            Map<Stat, Integer> stats = gp.getStats();

            if (target == null || !target.isOnline()) {
                p.sendMessage(GameAPI.getLangConfiguration().getStatCommandError(player));
                return true;
            }

            List<String> message = GameAPI.getLangConfiguration().getStatMessage(player);

            for (String m : message) {
                for (Stat s : Stat.values()) {
                    m = m.replaceAll("%"+s.getId()+"%", ""+stats.get(s));
                }

                p.sendMessage(m);
            }

            return true;
        });
	}

	@Override
	public List<String> getHelpMessage(CommandSender sender) {
		return new ArrayList<>();
	}

}
