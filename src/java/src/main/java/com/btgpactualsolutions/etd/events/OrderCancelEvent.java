package com.btgpactualsolutions.etd.events;

import java.io.Serializable;

import com.btgpactualsolutions.etd.core.FieldMapExtensions;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.*;

public class OrderCancelEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private String msgType;
	private String execType;
	private String ordStatus;
	private String orderID;
	private String clOrdID;
	private String origClOrdID;
	private String cxlRejResponseTo;
	private String text;
	private String account;
	private String symbol;
	private String noPartyIDss;

	/**
	 * 
	 * @param msg
	 * @throws NullPointerException
	 * @throws FieldNotFound
	 */
	public OrderCancelEvent(Message msg) throws NullPointerException, FieldNotFound {
		if (msg == null) {
			throw new NullPointerException();
		}

		msgType = FieldMapExtensions.tryGetValue(msg.getHeader(), MsgType.FIELD, String.class);
		execType = FieldMapExtensions.tryGetValue(msg, ExecType.FIELD, String.class);
		ordStatus = FieldMapExtensions.tryGetValue(msg, OrdStatus.FIELD, String.class);
		orderID = FieldMapExtensions.tryGetValue(msg, OrderID.FIELD, String.class);
		clOrdID = FieldMapExtensions.tryGetValue(msg, ClOrdID.FIELD, String.class);
		origClOrdID = FieldMapExtensions.tryGetValue(msg, OrigClOrdID.FIELD, String.class);
		cxlRejResponseTo = FieldMapExtensions.tryGetValue(msg, CxlRejResponseTo.FIELD, String.class);
		text = FieldMapExtensions.tryGetValue(msg, Text.FIELD, String.class);
		account = FieldMapExtensions.tryGetValue(msg, Account.FIELD, String.class);
		symbol = FieldMapExtensions.tryGetValue(msg, Symbol.FIELD, String.class);
	}

	@Override
	public String toString() {
		return "OrderCancelEvent:[msgType:" + msgType + "|execType:" + execType + "|ordStatus:" + ordStatus
				+ "|orderID:" + orderID + "|clOrdID:" + clOrdID + "|origClOrdID:" + origClOrdID + "|cxlRejResponseTo:"
				+ cxlRejResponseTo + "|text:" + text + "|account:" + account + "|symbol" + symbol + "|noPartyIDss:"
				+ noPartyIDss + "]";
	}

	public String getMsgType() {
		return msgType;
	}

	public String getExecType() {
		return execType;
	}

	public String getOrdStatus() {
		return ordStatus;
	}

	public String getOrderID() {
		return orderID;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public String getOrigClOrdID() {
		return origClOrdID;
	}

	public String getCxlRejResponseTo() {
		return cxlRejResponseTo;
	}

	public String getText() {
		return text;
	}

	public String getAccount() {
		return account;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getNoPartyIDss() {
		return noPartyIDss;
	}
}
