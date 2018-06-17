package net.upd4ting.gameapi.command.types;

import net.upd4ting.gameapi.command.ArgType;
import net.upd4ting.gameapi.command.Argument;

/**
 * Integer argument type
 * @author Upd4ting
 *
 */
public class IntegerType extends ArgType {

	public IntegerType() {
		super("%integer");
	}

	@Override
	public Checker constructChecker() {
		return (encodedArguments, index, args) -> {
            String argToEvalue = args[index];
            try {
                Integer number = Integer.parseInt(argToEvalue);
                encodedArguments.add(new Argument<>(number));
                return true;
            } catch (Exception e) { return false; }
        };
	}

}
