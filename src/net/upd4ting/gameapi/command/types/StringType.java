package net.upd4ting.gameapi.command.types;

import net.upd4ting.gameapi.command.ArgType;
import net.upd4ting.gameapi.command.Argument;

/**
 * String argument type
 * @author Upd4ting
 *
 */
public class StringType extends ArgType {

	public StringType() {
		super("%string");
	}

	@Override
	public Checker constructChecker() {
		return (encodedArguments, index, args) -> {
            StringBuilder sb = new StringBuilder();

            for (int i = index; i < args.length; i++)
                sb.append(args[i]).append(i < args.length - 1 ? " " : "");

            encodedArguments.add(new Argument<>(sb.toString()));

            return true;
        };
	}

}
