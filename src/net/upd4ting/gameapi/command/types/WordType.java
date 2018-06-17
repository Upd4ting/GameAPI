package net.upd4ting.gameapi.command.types;

import net.upd4ting.gameapi.command.ArgType;
import net.upd4ting.gameapi.command.Argument;

/**
 * Word argument type
 * @author Upd4ting
 *
 */
public class WordType extends ArgType {

	public WordType() {
		super("%word");
	}

	@Override
	public Checker constructChecker() {
		return (encodedArguments, index, args) -> {
            encodedArguments.add(new Argument<>(args[index]));
            return true;
        };
	}

}
