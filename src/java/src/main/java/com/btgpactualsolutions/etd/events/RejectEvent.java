package com.btgpactualsolutions.etd.events;

import com.btgpactualsolutions.etd.core.FieldMapExtensions;

import quickfix.Message;
import quickfix.field.ClOrdID;
import quickfix.field.MsgType;
import quickfix.field.RefMsgType;
import quickfix.field.RefTagID;
import quickfix.field.SessionRejectReason;
import quickfix.field.Text;

public class RejectEvent {

	private String msgType;
	private String clOrdID;
	private String refSeqNum;
	private String refMsgType;
	private String refTagID;
	private String sessionRejectReason;
	private String text;

	/**
	 * 
	 * @param msg
	 */
	public RejectEvent(Message msg) {
		if (msg == null) {
			throw new NullPointerException();
		}

		msgType = FieldMapExtensions.tryGetValue(msg.getHeader(), MsgType.FIELD, String.class);
		clOrdID = FieldMapExtensions.tryGetValue(msg, ClOrdID.FIELD, String.class);
		text = FieldMapExtensions.tryGetValue(msg, Text.FIELD, String.class);
		refMsgType = FieldMapExtensions.tryGetValue(msg, RefMsgType.FIELD, String.class);

		refTagID = FieldMapExtensions.tryGetValue(msg, RefTagID.FIELD, String.class);
		sessionRejectReason = FieldMapExtensions.tryGetValue(msg, SessionRejectReason.FIELD, String.class);
		refMsgType = FieldMapExtensions.tryGetValue(msg, RefMsgType.FIELD, String.class);
	}

	@Override
	public String toString() {
		return "RejectEvent:[msgType:" + msgType + "|clOrdID:" + clOrdID + "|refSeqNum:" + refSeqNum + "|refMsgType:"
				+ refMsgType + "|refTagID:" + refTagID + "|sessionRejectReason:" + sessionRejectReason + "|text:" + text
				+ "]";
	}

	public String getMsgType() {
		return msgType;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public String getRefSeqNum() {
		return refSeqNum;
	}

	public String getRefMsgType() {
		return refMsgType;
	}

	public String getRefTagID() {
		return refTagID;
	}

	public String getSessionRejectReason() {
		return sessionRejectReason;
	}

	public String getText() {
		return text;
	}
}
