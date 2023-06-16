package com.btgpactualsolutions.etd.events;

import java.math.BigDecimal;

import com.btgpactualsolutions.etd.core.FieldMapExtensions;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.AvgPx;
import quickfix.field.ClOrdID;
import quickfix.field.ClientID;
import quickfix.field.ContractMultiplier;
import quickfix.field.CumQty;
import quickfix.field.Currency;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.LeavesQty;
import quickfix.field.MaxFloor;
import quickfix.field.MinQty;
import quickfix.field.MsgType;
import quickfix.field.NoPartyIDs;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.PartyID;
import quickfix.field.PartyRole;
import quickfix.field.PositionEffect;
import quickfix.field.Price;
import quickfix.field.SenderSubID;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.field.WorkingIndicator;

public class ExecutionReportEvent {
	private String msgType;
	private String ordStatus;
	private String clOrdID;
	private String origClOrdID;
	private String orderID;
	private BigDecimal orderQty;
	private Side side;
	private OrdType ordType;
	private BigDecimal avgPx;
	private BigDecimal leavesQty;
	private BigDecimal cumQty;
	private String symbol;
	private String senderSubID;
	private String account;
	private String clientID;

	private String enteringTrader;
	private String internalID;
	private String execBroker;

	private String transactTime;
	private BigDecimal stopPx;
	private BigDecimal price;
	private String memo;
	private String currency;
	private String positionEffect;
	private String workingIndicator;
	private String contractMultiplie;
	private String maxFloor;
	private String minQty;
	private String execType;
	private String execID;
	private String text;
	private TimeInForce timeInForce;

	/**
	 * 
	 * @param msg
	 * @throws NullPointerException
	 * @throws FieldNotFound
	 */
	public ExecutionReportEvent(Message msg) throws NullPointerException, FieldNotFound {
		if (msg == null) {
			throw new NullPointerException();
		}

		msgType = FieldMapExtensions.tryGetValue(msg.getHeader(), MsgType.FIELD, String.class);
		clOrdID = FieldMapExtensions.tryGetValue(msg, ClOrdID.FIELD, String.class);
		origClOrdID = FieldMapExtensions.tryGetValue(msg, OrigClOrdID.FIELD, String.class);
		orderID = FieldMapExtensions.tryGetValue(msg, OrderID.FIELD, String.class);
		orderQty = FieldMapExtensions.tryGetValue(msg, OrderQty.FIELD, BigDecimal.class);
		side = new Side(FieldMapExtensions.tryGetValue(msg, Side.FIELD, Character.class));
		ordType = new OrdType(FieldMapExtensions.tryGetValue(msg, OrdType.FIELD, Character.class));
		avgPx = FieldMapExtensions.tryGetValue(msg, AvgPx.FIELD, BigDecimal.class);
		leavesQty = FieldMapExtensions.tryGetValue(msg, LeavesQty.FIELD, BigDecimal.class);
		cumQty = FieldMapExtensions.tryGetValue(msg, CumQty.FIELD, BigDecimal.class);
		symbol = FieldMapExtensions.tryGetValue(msg, Symbol.FIELD, String.class);
		senderSubID = FieldMapExtensions.tryGetValue(msg, SenderSubID.FIELD, String.class);
		account = FieldMapExtensions.tryGetValue(msg, Account.FIELD, String.class);
		clientID = FieldMapExtensions.tryGetValue(msg, ClientID.FIELD, String.class);
		transactTime = FieldMapExtensions.tryGetValue(msg, TransactTime.FIELD, String.class);
		price = FieldMapExtensions.tryGetValue(msg, Price.FIELD, BigDecimal.class);
		stopPx = FieldMapExtensions.tryGetValue(msg, StopPx.FIELD, BigDecimal.class);
		memo = FieldMapExtensions.tryGetValue(msg, 5149, String.class);
		currency = FieldMapExtensions.tryGetValue(msg, Currency.FIELD, String.class);
		positionEffect = FieldMapExtensions.tryGetValue(msg, PositionEffect.FIELD, String.class);
		workingIndicator = FieldMapExtensions.tryGetValue(msg, WorkingIndicator.FIELD, String.class);
		contractMultiplie = FieldMapExtensions.tryGetValue(msg, ContractMultiplier.FIELD, String.class);
		maxFloor = FieldMapExtensions.tryGetValue(msg, MaxFloor.FIELD, String.class);
		minQty = FieldMapExtensions.tryGetValue(msg, MinQty.FIELD, String.class);
		execType = FieldMapExtensions.tryGetValue(msg, ExecType.FIELD, String.class);
		execID = FieldMapExtensions.tryGetValue(msg, ExecID.FIELD, String.class);
		text = FieldMapExtensions.tryGetValue(msg, Text.FIELD, String.class);
		timeInForce = new TimeInForce(FieldMapExtensions.tryGetValue(msg, TimeInForce.FIELD, Character.class));

		for (int i = 1; i <= msg.getGroupCount(NoPartyIDs.FIELD); i++) {

			var group = msg.getGroup(i, NoPartyIDs.FIELD);

			var partyRole = FieldMapExtensions.tryGetValue(group, PartyRole.FIELD, Integer.class);
			var partyId = FieldMapExtensions.tryGetValue(group, PartyID.FIELD, String.class);

			switch (partyRole) {
			case PartyRole.ENTERING_TRADER:
				enteringTrader = partyId;
				break;
			case PartyRole.INTERESTED_PARTY:
				internalID = partyId;
				break;
			case PartyRole.ENTERING_FIRM:
				execBroker = partyId;
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "ExecutionReportEvent:[msgType:" + msgType + "|ordStatus:" + ordStatus + "|clOrdID:" + clOrdID
				+ "|origClOrdID:" + origClOrdID + "|orderID:" + orderID + "|orderQty:" + orderQty + "|side:" + side
				+ "|ordType:" + ordType + "|avgPx:" + avgPx + "|leavesQty:" + leavesQty + "|cumQty:" + cumQty
				+ "|symbol:" + symbol + "|senderSubID:" + senderSubID + "|account:" + account + "|clientID:" + clientID
				+ "|enteringTrader:" + enteringTrader + "|internalID:" + internalID + "|execBroker:" + execBroker
				+ "|transactTime:" + transactTime + "|stopPx:" + stopPx + "|price:" + price + "|memo:" + memo
				+ "|currency:" + currency + "|positionEffect:" + positionEffect + "|workingIndicator:"
				+ workingIndicator + "|contractMultiplie:" + contractMultiplie + "|maxFloor:" + maxFloor + "|minQty:"
				+ minQty + "|execType:" + execType + "|execID:" + execID + "|text:" + text + "|timeInForce:"
				+ timeInForce + "]";
	}

	public String getMsgType() {
		return msgType;
	}

	public String getOrdStatus() {
		return ordStatus;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public String getOrigClOrdID() {
		return origClOrdID;
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public Side getSide() {
		return side;
	}

	public OrdType getOrdType() {
		return ordType;
	}

	public BigDecimal getAvgPx() {
		return avgPx;
	}

	public BigDecimal getLeavesQty() {
		return leavesQty;
	}

	public BigDecimal getCumQty() {
		return cumQty;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getSenderSubID() {
		return senderSubID;
	}

	public String getAccount() {
		return account;
	}

	public String getClientID() {
		return clientID;
	}

	public String getEnteringTrader() {
		return enteringTrader;
	}

	public String getInternalID() {
		return internalID;
	}

	public String getExecBroker() {
		return execBroker;
	}

	public String getTransactTime() {
		return transactTime;
	}

	public BigDecimal getStopPx() {
		return stopPx;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getMemo() {
		return memo;
	}

	public String getCurrency() {
		return currency;
	}

	public String getPositionEffect() {
		return positionEffect;
	}

	public String getWorkingIndicator() {
		return workingIndicator;
	}

	public String getContractMultiplie() {
		return contractMultiplie;
	}

	public String getMaxFloor() {
		return maxFloor;
	}

	public String getMinQty() {
		return minQty;
	}

	public String getExecType() {
		return execType;
	}

	public String getExecID() {
		return execID;
	}

	public String getText() {
		return text;
	}

	public TimeInForce getTimeInForce() {
		return timeInForce;
	}
}
