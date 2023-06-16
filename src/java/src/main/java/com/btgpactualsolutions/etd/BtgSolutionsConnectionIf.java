package com.btgpactualsolutions.etd;

import com.btgpactualsolutions.etd.config.ConnectionTimeOutException;
import com.btgpactualsolutions.etd.config.NotConnectedException;
import com.btgpactualsolutions.etd.model.CreateNewOrderSingle;
import com.btgpactualsolutions.etd.model.CreateOrderCancel;
import com.btgpactualsolutions.etd.model.CreateOrderReplace;

import quickfix.Message;

public interface BtgSolutionsConnectionIf {
	/**
	 * Start Connection
	 * 
	 * @throws ConnectionTimeOutException
	 * @throws NotConnectedException
	 */
	void start() throws ConnectionTimeOutException, NotConnectedException;

	/**
	 * Stop Connection
	 */
	void stop();

	/**
	 * Send custom fix message
	 * @param message
	 * @throws NullPointerException
	 * @throws NotConnectedException
	 */
	void sendFixMessage(Message message) throws NullPointerException, NotConnectedException;
	
	/**
	 * Send new order
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	void newOrderSingle(CreateNewOrderSingle req) throws NotConnectedException, NullPointerException;

	/**
	 * Send order replace
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	void orderReplaceRequest(CreateOrderReplace req) throws NotConnectedException, NullPointerException;

	/**
	 * Send order cancel
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	void orderCancelRequest(CreateOrderCancel req) throws NotConnectedException, NullPointerException;

	/**
	 * Listen for events
	 * 
	 * @param handler
	 * @throws NullPointerException
	 */
	void registerHandleEvents(HandleEventIf handler) throws NullPointerException;
}
