package net.upd4ting.gameapi.exception;

public class InvalidConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidConfigException(String error) {
		super(error);
	}
}
