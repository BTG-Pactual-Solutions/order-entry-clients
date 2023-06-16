package com.btgpactualsolutions.etd.core;

import quickfix.FieldNotFound;
import quickfix.Message;

public interface HandlerMessageIf {
	/**
	 * Handle fix message
	 * @param message
	 * @throws NullPointerException
	 * 		thrown if message is null
	 * @throws FieldNotFound
	 *  	thrown if expected fields was not find
	 */
	void handleMessage(Message message) throws NullPointerException, FieldNotFound;
}
