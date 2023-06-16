package com.btgpactualsolutions.etd.config;

public class NotConnectedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5128622543204859308L;

	public NotConnectedException() {
	}

	/**
	 * 
	 * @param message
	 */
	public NotConnectedException(String message) {
		super(message);
	}

}
