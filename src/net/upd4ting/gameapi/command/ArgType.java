package net.upd4ting.gameapi.command;

import java.util.HashMap;
import java.util.List;

/**
 * Class to extends to create different argument type in the command
 * @author Upd4ting
 *
 */
public abstract class ArgType {
	public interface Checker { Boolean check(List<Argument<?>> encodedArguments, Integer index, String[] args); }
	public static final HashMap<String, ArgType> argTypes = new HashMap<>();
	
	private final String format;
	private final Checker checker;
	
	public ArgType(String format) {
		this.format = format;
		this.checker = constructChecker();
		argTypes.put(format, this);
	}
	
	public abstract Checker constructChecker();
	
	public String getFormat() {
		return format;
	}
	
	public Checker getChecker() {
		return checker;
	}
	
	public static Boolean isFormat(String text) {
		return argTypes.keySet().contains(text);
	}
	
	public static Boolean executeChecker(String format, List<Argument<?>> encodedArguments, Integer index, String[] args) {
		return argTypes.get(format).getChecker().check(encodedArguments, index, args);
	}
	
	public static void register(ArgType arg) {
		argTypes.put(arg.getFormat(), arg);
	}
}
