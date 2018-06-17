package net.upd4ting.gameapi;

/**
 * This class store the result of the join
 * @author Upd4ting
 *
 */
public class JoinResult {
	
	private final boolean hasJoined;
	private final String message;
	
	/**
	 * Default constructor
	 * @param hasJoined The boolean
	 */
	public JoinResult(boolean hasJoined) {
		this.hasJoined = hasJoined;
		this.message = null;
	}
	
	/**
	 * Constructor with a message
	 * Usually used to set an error message
	 * @param hasJoined The boolean
	 * @param message The message
	 */
	public JoinResult(boolean hasJoined, String message) {
		this.hasJoined = hasJoined;
		this.message = message;
	}
	
	/**
	 * Know if the player has joined the game or not
	 * @return true/false
	 */
	public boolean hasJoined() {
		return hasJoined;
	}
	
	/**
	 * Get the error message
	 * @return The error message
	 */
	public String getMessage() {
		return message;
	}
}
