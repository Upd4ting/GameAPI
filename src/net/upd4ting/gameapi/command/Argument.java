package net.upd4ting.gameapi.command;

/**
 * Argument of a command
 * @author Upd4ting
 */
public class Argument<T> {
	
	private final T element;
	
	public Argument(T element) {
		this.element = element;
	}
	
	public Integer getAsInteger() {
		return (Integer)element;
	}
	
	public String getAsString() {
		return (String)element;
	}
	
	public Long getAsLong() {
		return Long.parseLong(Integer.toString(getAsInteger()));
	}
	
	@Override
	public String toString() {
		return element.toString();
	}
}
