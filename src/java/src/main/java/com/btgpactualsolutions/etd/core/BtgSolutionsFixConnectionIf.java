package com.btgpactualsolutions.etd.core;

import com.btgpactualsolutions.etd.config.ConnectionTimeOutException;
import com.btgpactualsolutions.etd.config.NotConnectedException;

import quickfix.Application;
import quickfix.Message;

public interface BtgSolutionsFixConnectionIf extends Application {
	/**
	 * Start fix connection
	 * @throws ConnectionTimeOutException
	 * @throws NotConnectedException
	 */
	void start() throws ConnectionTimeOutException, NotConnectedException;

	/**
	 * Stop fix connection
	 */
	void stop();

	/**
	 * Send fix message
	 * @param msg
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	void sendMessage(Message msg) throws NotConnectedException, NullPointerException;

	/**
	 * Get if message is connected
	 * @return
	 */
	boolean getIsConnect();

}
