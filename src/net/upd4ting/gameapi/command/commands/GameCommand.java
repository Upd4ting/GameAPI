package net.upd4ting.gameapi.command.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import net.upd4ting.gameapi.*;
import net.upd4ting.gameapi.inventory.inv.config.InvListConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.upd4ting.gameapi.command.Command;
import net.upd4ting.gameapi.GameAPI.GamePlugin;

public class GameCommand extends Command {

	private static final String SEPARATOR = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + Strings.repeat("-", 51);

	public GameCommand() {
		super("game", null, false);
	}

	@Override
	public void constructCommands() {
	    this.addAlias("g", "game");

		this.addCommandAction("", (sender, args) -> {
		    return false; // So will send hepl
        });
		
		this.addCommandAction("info", (sender, args) -> {
            Player p = (Player) sender;

            if (!p.hasPermission("game.admin"))
                return true;

            p.sendMessage("§3Licensed to §6- §7" + GameAPI.uid);
            p.sendMessage("§3Licensed SpigotMC Link §6- §7" + GameAPI.uidspigotlink);
            p.sendMessage("§3Resource ID to §6- §7" + GameAPI.RESOURCE);
            p.sendMessage("§3Plugin Version to §6- §7" + GameAPI.getInstance().getDescription().getVersion());

            return true;
        });
		
		this.addCommandAction("start", (sender, args) -> {
            Player p = (Player) sender;

            if (!p.hasPermission("game.admin"))
                return true;

            Game game = GameManager.getPlayerGame(p);

            if (game == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a game !");
                return true;
            }

            Countdown countdown = game.getCountdown();
            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (countdown.isStarted() || !game.isWaiting())
                sender.sendMessage(prefix + ChatColor.RED + "Game already started!");
            else {
                countdown.start(true);
                sender.sendMessage(prefix + ChatColor.GREEN + "Game started!");
            }

            return true;
        });
		
		this.addCommandAction("forcestart", (sender, args) -> {
            Player p = (Player) sender;

            if (!p.hasPermission("game.admin"))
                return true;

            Game game = GameManager.getPlayerGame(p);

            if (game == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a game !");
                return true;
            }

            Countdown countdown = game.getCountdown();
            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (countdown.isStarted() || !game.isWaiting())
                sender.sendMessage(prefix+ ChatColor.RED + "Game already started!");
            else
                game.start();

            return true;
        });


		// Game creation command

		this.addCommandAction("create %word %word", (sender, args) -> {

            if (!sender.hasPermission("game.admin"))
                return true;

		    if (GameAPI.getGameConfiguration().isBungeeMode())
		        return false;

            String name = args.get(0).getAsString();
            String worldName = args.get(1).getAsString();

            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (GameManager.getGame(name) != null) {
                sender.sendMessage(prefix + ChatColor.RED + "This game name already exists !");
                return true;
            }

            if (Bukkit.getWorld(worldName) == null) {
                sender.sendMessage(prefix + ChatColor.RED + "This world name doesn't exist !");
                return true;
            }

            if (GameManager.getGameByWorld(worldName) != null) {
                sender.sendMessage(prefix + ChatColor.RED + "There is already a game in this world !");
                return true;
            }

            GamePlugin gp = (GamePlugin) GameAPI.getInstance();

            GameManager.registerGame(gp.createGame(Bukkit.getWorld(worldName), name));

            sender.sendMessage(prefix + ChatColor.GREEN + "Game has been created !");

            return true;
        });

		this.addCommandAction("remove %word", (sender, args) -> {

            if (!sender.hasPermission("game.admin"))
                return true;

            if (GameAPI.getGameConfiguration().isBungeeMode())
                return false;

            String name = args.get(0).getAsString();

            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (GameManager.getGame(name) == null) {
                sender.sendMessage(prefix + ChatColor.RED + "This game name doesn't exists !");
                return true;
            }

            Game game = GameManager.getGame(name);

            game.close(false);

            GameManager.unregisterGame(game);

            GameSaver.remove(game);

            sender.sendMessage(ChatColor.GREEN + "Game removed !");

            return true;
        });

		this.addCommandAction("list", (sender, args) -> {

            if (!sender.hasPermission("game.admin"))
                return true;

            StringBuilder sb = new StringBuilder();

            String prefix = GameAPI.getLangConfiguration().getPrefix();

            for (Game game : GameManager.getGames()) {
                sb.append(game.getName()).append(",");
            }

            String message = sb.toString();

            if (message.length() > 0) // Remove last virgule
                message = message.substring(0, message.length() - 1);

            sender.sendMessage(prefix + ChatColor.GOLD + "List: " + ChatColor.GRAY + message);

            return true;
        });

		this.addCommandAction("config %word", (sender, args) -> {

            if (!sender.hasPermission("game.admin"))
                return true;

            String name = args.get(0).getAsString();

            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (GameManager.getGame(name) == null) {
                sender.sendMessage(prefix + ChatColor.RED + "This game name doesn't exists !");
                return true;
            }

            Game game = GameManager.getGame(name);

            if (!game.isWaiting()) {
                sender.sendMessage(prefix + ChatColor.RED + "This game is running...");
                return true;
            }

            new InvListConfig((Player) sender, game).open();

            return true;
        });

		this.addCommandAction("createworld %word", (sender, args) -> {

            if (!sender.hasPermission("game.admin"))
                return true;

            if (GameAPI.getGameConfiguration().isBungeeMode())
                return false;

            String name = args.get(0).getAsString();

            String prefix = GameAPI.getLangConfiguration().getPrefix();

            if (Bukkit.getWorld(name) != null) {
                sender.sendMessage(prefix + ChatColor.RED + "This world already exist !");
                return true;
            }

            WorldCreator wc = new WorldCreator(name);
            wc.createWorld();

            sender.sendMessage(prefix + ChatColor.GREEN + "World created !");

            return true;
        });

		this.addCommandAction("leave", ((sender, args) -> {
		    Player p = (Player) sender;

		    Game game = GameManager.getPlayerGame(p);

		    if (game == null)
		        return true;

		    game.expulse(p, null);

		    return true;
        }));
	}

	@Override
	public List<String> getHelpMessage(CommandSender sender) {
		Player p = (Player) sender;
		List<String> list = new ArrayList<>();

		list.add(SEPARATOR);

		if (sender.hasPermission("game.admin")) {
            list.add(ChatColor.YELLOW + "/game start: " + ChatColor.GRAY + "Start the game.");
            list.add(ChatColor.YELLOW + "/game forcestart: " + ChatColor.GRAY + "Forcestart the game without any timers.");

            if (!GameAPI.getGameConfiguration().isBungeeMode()) {
                list.add(ChatColor.YELLOW + "/game create <name> <worldname>: " + ChatColor.GRAY + "Create a game.");
                list.add(ChatColor.YELLOW + "/game remove <name>: " + ChatColor.GRAY + "Remove a game.");
                list.add(ChatColor.YELLOW + "/game createworld <world>: " + ChatColor.GRAY + "Create a world.");
            }

            list.add(ChatColor.YELLOW + "/game list: " + ChatColor.GRAY + "List all the games currently registred.");
            list.add(ChatColor.YELLOW + "/game config <name>: " + ChatColor.GRAY + "Configure a game.");
        }

        list.add(ChatColor.YELLOW + "/game leave: " + ChatColor.GRAY + "Leave the game.");
		
		Game game = GameManager.getPlayerGame(p);
		
		if (game != null && game.getCommandHandler() != null)
			list.addAll(game.getCommandHandler().getAdditionnalHelpCommand());
		
		list.add(SEPARATOR);
		
		return list;
	}
}
