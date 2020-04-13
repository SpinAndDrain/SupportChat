package de.spinanddrain.supportchat.external.sql.exception;

public class ConnectionFailedException extends Exception {

	/*
	 * Created by SpinAndDrain on 15.09.2019
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 5849439803974579714L;

	public ConnectionFailedException(String message) {
		super(message);
	}
	
}
