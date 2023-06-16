package com.btgpactualsolutions.etd.config;

public class ConnectionTimeOutException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3353053487015967564L;

	public ConnectionTimeOutException() {		
	}
	
	public ConnectionTimeOutException(String message) {
        super(message);
    }
}
