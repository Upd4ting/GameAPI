package net.upd4ting.gameapi.command.types;

import net.upd4ting.gameapi.command.ArgType;
import net.upd4ting.gameapi.command.Argument;

/**
 * Time argument type
 * @author Upd4ting
 *
 */
public class TimeType extends ArgType {

	public TimeType() {
		super("%time");
	}

	@Override
	public Checker constructChecker() {
		return (encodedArguments, index, args) -> {
            String input = args[index];
            Integer time = 0;

            if (input.equals("permanent") || input.equals("perm")) {
                time = -1;
            } else {
                if (input.contains("mo")) {
                    Integer numberMonth = getNumber(input, "mo");
                    if (numberMonth == null)
                        return false;
                    time += numberMonth * 30 * 24 * 60 * 60;
                    Integer index2 = input.indexOf("mo") + 2;
                    input = input.substring(index2 >= input.length() ? input.length() : index2);
                } if (input.contains("d")) {
                    Integer numberDay = getNumber(input, "d");
                    if (numberDay == null)
                        return false;
                    time += numberDay * 24 * 60 * 60;
                    Integer index2 = input.indexOf("d") + 1;
                    input = input.substring(index2 >= input.length() ? input.length() : index2);
                } if (input.contains("h")) {
                    Integer numberHour = getNumber(input, "h");
                    if (numberHour == null)
                        return false;
                    time += numberHour * 60 * 60;
                    Integer index2 = input.indexOf("h") + 1;
                    input = input.substring(index2 >= input.length() ? input.length() : index2);
                } if(input.contains("m")) {
                    Integer numberMin = getNumber(input, "m");
                    if (numberMin == null)
                        return false;
                    time += numberMin * 60;
                    Integer index2 = input.indexOf("m") + 1;
                    input = input.substring(index2 >= input.length() ? input.length() : index2);
                } if(input.contains("s")) {
                    Integer numberSec = getNumber(input, "s");
                    if (numberSec == null)
                        return false;
                    time += numberSec;
                    Integer index2 = input.indexOf("s") + 1;
                    input = input.substring(index2 >= input.length() ? input.length() : index2);
                }
            }

            if (time != 0)
                encodedArguments.add(new Argument<>(time));

            return time != 0;
        };
	}
	
	private Integer getNumber(String input, String toFind) {
		Integer index = input.indexOf(toFind);
		
		String timeString = input.substring(0, index);
		Integer numberMonth;
		try {
			numberMonth = Integer.parseInt(timeString);
		} catch (Exception e) { return null; }
		
		return numberMonth;
	}

}
