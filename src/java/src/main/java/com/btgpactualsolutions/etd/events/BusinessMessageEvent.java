package com.btgpactualsolutions.etd.events;

import com.btgpactualsolutions.etd.core.FieldMapExtensions;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.MsgType;
import quickfix.field.Text;

public class BusinessMessageEvent {
	private String msgType;
	private String businessRejectReason;
	private String clOrdID;
	private String text;
	private String refMsgType;

	/**
	 * 
	 * @param msg
	 * @throws NullPointerException
	 * @throws FieldNotFound
	 */
	public BusinessMessageEvent(Message msg) throws NullPointerException, FieldNotFound {
		if (msg == null) {
			throw new NullPointerException();
		}

		msgType = FieldMapExtensions.tryGetValue(msg.getHeader(), MsgType.FIELD, String.class);
		clOrdID = FieldMapExtensions.tryGetValue(msg, ClOrdID.FIELD, String.class);
		text = FieldMapExtensions.tryGetValue(msg, Text.FIELD, String.class);
		refMsgType = FieldMapExtensions.tryGetValue(msg, Text.FIELD, String.class);
		businessRejectReason = FieldMapExtensions.tryGetValue(msg, BusinessRejectReason.FIELD, String.class);
	}

	@Override
	public String toString() {
		return "BusinessMessageEvent:[msgType:" + msgType + "|businessRejectReason:" + businessRejectReason + "|clOrdID:" + clOrdID + "|text:" + text + "|refMsgType:" + refMsgType + "]";
	}

	public String getMsgType() {
		return msgType;
	}

	public String getBusinessRejectReason() {
		return businessRejectReason;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public String getText() {
		return text;
	}

	public String getRefMsgType() {
		return refMsgType;
	}
}
