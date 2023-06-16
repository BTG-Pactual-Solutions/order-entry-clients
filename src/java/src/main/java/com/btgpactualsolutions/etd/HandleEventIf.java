package com.btgpactualsolutions.etd;

import com.btgpactualsolutions.etd.events.BusinessMessageEvent;
import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import com.btgpactualsolutions.etd.events.OrderCancelEvent;
import com.btgpactualsolutions.etd.events.RejectEvent;

import quickfix.Message;

public abstract interface HandleEventIf {
	/**
	 * handle fix message
	 * @param message
	 */
	void observableMessage(Message message);

	/**
	 * handle parsed execution report
	 * @param event
	 */
	void observableExecutionReport(ExecutionReportEvent event);

	/**
	 * handle parsed order cancel
	 * @param event
	 */
	void observableOrderCancel(OrderCancelEvent event);

	/**
	 * handle parsed business message reject
	 * @param event
	 */
	void observableBusinessMessageReject(BusinessMessageEvent event);

	/**
	 * handle parsed reject
	 * @param event
	 */
	void observableReject(RejectEvent event);

	/**
	 * handle connection status change to or from active
	 * @param event
	 */
	void observableIsSessionAlive(boolean event);
}