package net.upd4ting.gameapi.command;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

import net.upd4ting.gameapi.command.CommandManager.CommandAction;

/**
 * Class to extends to create a command
 * @author Upd4ting
 *
 */
public abstract class Command {
	
	private final String cmd;
	private final HashMap<String, CommandAction> actions = new HashMap<>();
	private final HashMap<String, String> aliases = new HashMap<>();
	private final String perm;
	private final Boolean canBePerformedByConsole;
	
	public Command(String cmd, String perm, Boolean console) {
		this.cmd = cmd;
		this.perm = perm;
		this.canBePerformedByConsole = console;
		constructCommands();
	}
	
	public abstract void constructCommands();
	public abstract List<String> getHelpMessage(CommandSender sender);
	
	public String getCmd() {
		return this.cmd;
	}
	
	public HashMap<String, CommandAction> getActions() {
		return this.actions;
	}
	
	public HashMap<String, String> getAliases() {
		return this.aliases;
	}
	
	public String getPerm() {
		return this.perm;
	}
	
	public Boolean canBePerformedByConsole() {
		return this.canBePerformedByConsole;
	}
	
	public void addCommandAction(String arg, CommandAction action) {
		this.actions.put(arg, action);
	}
	
	public void addAlias(String from, String to) {
		this.aliases.put(from, to);
	}
}
