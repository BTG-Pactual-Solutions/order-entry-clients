package com.btgpactualsolutions.etd;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.btgpactualsolutions.etd.config.ConnectionTimeOutException;
import com.btgpactualsolutions.etd.config.FixSettings;
import com.btgpactualsolutions.etd.config.NotConnectedException;
import com.btgpactualsolutions.etd.core.BtgSolutionsFixConnectionIf;
import com.btgpactualsolutions.etd.core.BtgSolutionsFixConnectionImpl;
import com.btgpactualsolutions.etd.core.HandlerMessageIf;
import com.btgpactualsolutions.etd.events.BusinessMessageEvent;
import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import com.btgpactualsolutions.etd.events.OrderCancelEvent;
import com.btgpactualsolutions.etd.events.RejectEvent;
import com.btgpactualsolutions.etd.model.CreateNewOrderSingle;
import com.btgpactualsolutions.etd.model.CreateOrderCancel;
import com.btgpactualsolutions.etd.model.CreateOrderReplace;
import com.btgpactualsolutions.etd.model.EvaluateResult;

import quickfix.CharField;
import quickfix.ConfigError;
import quickfix.DecimalField;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.IntField;
import quickfix.Message;
import quickfix.StringField;
import quickfix.UtcTimeStampField;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.ClientID;
import quickfix.field.Country;
import quickfix.field.Currency;
import quickfix.field.DisplayQty;
import quickfix.field.ExecBroker;
import quickfix.field.HandlInst;
import quickfix.field.MinQty;
import quickfix.field.MsgType;
import quickfix.field.NoPartyIDs;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.field.Price;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SenderSubID;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.Symbol;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;

public class BtgSolutionsConnectionImpl implements BtgSolutionsConnectionIf, HandlerMessageIf {

	private static final Logger log = LoggerFactory.getLogger(BtgSolutionsConnectionImpl.class);

	private HandleEventIf _fixMessageIf;
	private BtgSolutionsFixConnectionIf _fixConnection;

	private String _defaultClientId;
	
	/**
	 * @param senderCompID
	 * @param targetCompID
	 * @param socketConnectHost
	 * @param socketConnectPort
	 * @param SSLCertificate
	 * @param username
	 * @param password
	 * @param defaultClientId
	 * @param settings
	 * @throws ConfigError
	 */
	public BtgSolutionsConnectionImpl(String senderCompID, String targetCompID, String socketConnectHost,
			int socketConnectPort, String SSLCertificate, String username, String password, String defaultClientId, FixSettings settings)
			throws ConfigError {
		_defaultClientId = defaultClientId;
		_fixConnection = new BtgSolutionsFixConnectionImpl(this, username, password, senderCompID, targetCompID,
				socketConnectHost, socketConnectPort, SSLCertificate, settings);
	}

	/**
	 * Start Stop Connection
	 */
	@Override
	public void start() throws ConnectionTimeOutException, NotConnectedException {
		log.info("Starting Btg Solutions connection");
		_fixConnection.start();
		log.info("Btg Solutions connection started");
	}

	/**
	 * Stop Connection
	 */
	@Override
	public void stop() {
		log.info("Stopping Btg Solutions connection");
		_fixConnection.stop();
		log.info("Btg Solutions connection stopped");
	}

	/**
	 * Send new order
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	@Override
	public void newOrderSingle(CreateNewOrderSingle req) throws NotConnectedException, NullPointerException {
		log.info("Creating message new order single");
		if(stringIsNullOrEmpty(req.getClientId()) && !stringIsNullOrEmpty(_defaultClientId)) {
			req.setClientId(_defaultClientId);
		}
		EvaluateResult evaluateResult = req.evaluate();
		if(evaluateResult.hasIssue()) {
			throw new NullPointerException(evaluateResult.getIssue());
		}

		Message message = buildMessage(MsgType.ORDER_SINGLE);

		// SenderSubID
		message.setField(new StringField(SenderSubID.FIELD, req.getTrader()));

		// ClientID
		message.setField(new StringField(ClientID.FIELD, req.getClientId()));

		// Symbol
		message.setField(new StringField(Symbol.FIELD, req.getSymbol()));

		// Security settings
		setIfHasValue(message, SecurityExchange.FIELD, req.getExDestination(), "BVMF");
		setIfHasValue(message, SecurityIDSource.FIELD, req.getSecurityIdSource(), SecurityIDSource.EXCHANGE_SYMBOL);
		setIfHasValue(message, SecurityID.FIELD, req.getSecurityId(), req.getSymbol());

		// Side
		message.setField(new CharField(Side.FIELD, req.getSide().getValue()));

		// Account
		setIfHasValue(message, Account.FIELD, req.getAccount());

		// ExecBroker
		if (req.getExecBroker() != null) {
			message.setField(new IntField(ExecBroker.FIELD, req.getExecBroker()));
		}

		// ClOrdId
		if (req.getClOrdID() != null && !req.getClOrdID().isEmpty()) {
			message.setField(new StringField(ClOrdID.FIELD, req.getClOrdID()));
		} else {
			req.setClOrdID(UUID.randomUUID().toString().replace("-", ""));
			message.setField(new StringField(ClOrdID.FIELD, req.getClOrdID()));
		}

		// HandlInst
		if (req.isDMA()) {
			message.setField(
					new CharField(HandlInst.FIELD, HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION));
		} else {
			message.setField(new CharField(HandlInst.FIELD, HandlInst.MANUAL_ORDER_BEST_EXECUTION));
		}

		// TimeInForce
		if (req.getTimeInForce() != null) {
			message.setField(new CharField(TimeInForce.FIELD, req.getTimeInForce().getValue()));
		} else {
			message.setField(new CharField(TimeInForce.FIELD, TimeInForce.DAY));
		}

		// CCY
		setIfHasValue(message, Currency.FIELD, req.getCurrency(), "BRL");

		// Country
		setIfHasValue(message, Country.FIELD, req.getCountry());

		// OrdType
		Character valueOrderType;
		if(req.getOrdType() != null) {
			valueOrderType = req.getOrdType().getValue();
		} else {
			valueOrderType = OrdType.LIMIT;
		}
		setIfHasValue(message, OrdType.FIELD, valueOrderType);

		// Qty
		setIfHasValue(message, OrderQty.FIELD, req.getOrderQty());
		setIfHasValue(message, MinQty.FIELD, req.getMinQty());
		setIfHasValue(message, DisplayQty.FIELD, req.getDisplayQty());
		setIfHasValue(message, StopPx.FIELD, req.getStopPx());

		// Price
		setIfHasValue(message, Price.FIELD, req.getPrice());

		// NoPartyIDs
		int i = 1;
		Group groupEnteringTrader = new Group(NoPartyIDs.FIELD, i++);
		groupEnteringTrader.setField(new IntField(PartyRole.FIELD, PartyRole.ENTERING_TRADER));
		groupEnteringTrader.setField(new CharField(PartyIDSource.FIELD, PartyIDSource.PROPRIETARY_CUSTOM_CODE));
		groupEnteringTrader.setField(new StringField(PartyID.FIELD, req.getTrader()));
		message.addGroup(groupEnteringTrader);
		if (req.getExecBroker() != null) {
			Group groupEnteringFirm = new Group(NoPartyIDs.FIELD, i++);
			groupEnteringFirm.setField(new IntField(PartyRole.FIELD, PartyRole.ENTERING_FIRM));
			groupEnteringFirm.setField(new CharField(PartyIDSource.FIELD, PartyIDSource.PROPRIETARY_CUSTOM_CODE));
			groupEnteringFirm.setField(new IntField(PartyID.FIELD, req.getExecBroker()));
			message.addGroup(groupEnteringFirm);
		}

		_fixConnection.sendMessage(message);
		log.info("Success create message new order single");
	}

	/**
	 * Send order replace
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	@Override
	public void orderReplaceRequest(CreateOrderReplace req) throws NotConnectedException, NullPointerException {
		log.info("Creating message order replace request");
		if(stringIsNullOrEmpty(req.getClientId()) && !stringIsNullOrEmpty(_defaultClientId)) {
			req.setClientId(_defaultClientId);
		}
		EvaluateResult evaluateResult = req.evaluate();
		if(evaluateResult.hasIssue()) {
			throw new NullPointerException(evaluateResult.getIssue());
		}
		Message message = buildMessage(MsgType.ORDER_CANCEL_REPLACE_REQUEST);

		// ClientID
		message.setField(new StringField(ClientID.FIELD, req.getClientId()));

		// Security settings
		setIfHasValue(message, SecurityExchange.FIELD, req.getExDestination());
		setIfHasValue(message, SecurityIDSource.FIELD, req.getSecurityIdSource());
		setIfHasValue(message, SecurityID.FIELD, req.getSecurityId());

		// Side
		message.setField(new CharField(Side.FIELD, req.getSide().getValue()));

		// OrigClOrdID
		message.setField(new StringField(OrigClOrdID.FIELD, req.getOrigClOrdID()));

		// ClOrdId
		setIfHasValue(message, ClOrdID.FIELD, req.getClOrdID(), UUID.randomUUID().toString().replace("-", ""));

		// TimeInForce
		Character valueTimeInForce;
		if(req.getTimeInForce() != null) {
			valueTimeInForce = req.getTimeInForce().getValue();
		} else {
			valueTimeInForce = null;
		}
		setIfHasValue(message, TimeInForce.FIELD, valueTimeInForce);

		// CCY
		setIfHasValue(message, Currency.FIELD, req.getCurrency());

		// Country
		setIfHasValue(message, Country.FIELD, req.getCountry());

		// OrdType
		Character valueOrderType;
		if(req.getOrdType() != null) {
			valueOrderType = req.getOrdType().getValue();
		} else {
			valueOrderType = null;
		}
		setIfHasValue(message, OrdType.FIELD, valueOrderType);

		// Qty
		setIfHasValue(message, MinQty.FIELD, req.getMinQty());
		setIfHasValue(message, OrderQty.FIELD, req.getOrderQty());
		setIfHasValue(message, DisplayQty.FIELD, req.getDisplayQty());
		setIfHasValue(message, StopPx.FIELD, req.getStopPx());

		// Price
		setIfHasValue(message, Price.FIELD, req.getPrice());

		_fixConnection.sendMessage(message);
		log.info("Success on create message order replace request");
	}

	/**
	 * Send order cancel
	 * 
	 * @param req
	 * @throws NotConnectedException
	 * @throws NullPointerException
	 */
	public void orderCancelRequest(CreateOrderCancel req) throws NotConnectedException, NullPointerException {
		log.info("Creating message order cancel request");
		if(stringIsNullOrEmpty(req.getClientId()) && !stringIsNullOrEmpty(_defaultClientId)) {
			req.setClientId(_defaultClientId);
		}
		EvaluateResult evaluateResult = req.evaluate();
		if(evaluateResult.hasIssue()) {
			throw new NullPointerException(evaluateResult.getIssue());
		}
		Message message = buildMessage(MsgType.ORDER_CANCEL_REQUEST);

		// ClientId
		message.setField(new StringField(ClientID.FIELD, req.getClientId()));

		// OrderID
		setIfHasValue(message, OrderID.FIELD, req.getOrderID());

		// ClOrdId
		setIfHasValue(message, ClOrdID.FIELD, req.getClOrdID(), UUID.randomUUID().toString().replace("-", ""));

		// OrigClOrdID
		setIfHasValue(message, OrigClOrdID.FIELD, req.getOrigClOrdID());

		// Side
		message.setField(new CharField(Side.FIELD, req.getSide().getValue()));

		// OrderQty is required if HandlInst is MANUAL_ORDER (care)
		setIfHasValue(message, OrderQty.FIELD, req.getOrderQty());

		_fixConnection.sendMessage(message);
		log.info("Success Create message order cancel request");
	}

	private Message buildMessage(String messageType) {
		Message message = new Message();
		message.getHeader().setField(new MsgType(messageType));

		// TransactTime
		message.setField(new UtcTimeStampField(TransactTime.FIELD));
		return message;
	}

	private boolean stringIsNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

	private void setIfHasValue(Message message, int tag, String value) {
		if (!stringIsNullOrEmpty(value)) {
			message.setField(new StringField(tag, value));
		}
	}

	private void setIfHasValue(Message message, int tag, String value, String defaultValue) {
		if (!stringIsNullOrEmpty(value)) {
			message.setField(new StringField(tag, value));
		}
		else if (!stringIsNullOrEmpty(defaultValue)) {
			message.setField(new StringField(tag, defaultValue));
		}
	}
	
	private void setIfHasValue(Message message, int tag, Integer value) {
		if (value != null) {
			message.setField(new IntField(tag, value));
		}
	}

	private void setIfHasValue(Message message, int tag, Character value) {
		if (value != null) {
			message.setField(new CharField(tag, value));
		}
	}

	private void setIfHasValue(Message message, int tag, BigDecimal value) {
		if (value != null) {
			message.setField(new DecimalField(tag, value));
		}
	}

	/**
	 * Handle fix message
	 * @param message
	 * @throws NullPointerException
	 * 		thrown if message is null
	 * @throws FieldNotFound
	 *  	thrown if expected fields was not find
	 */
	@Override
	public void handleMessage(Message message) throws NullPointerException, FieldNotFound {
		if (_fixMessageIf != null) {
			switch (message.getHeader().getString(MsgType.FIELD)) {
			case MsgType.EXECUTION_REPORT:
				_fixMessageIf.observableExecutionReport(new ExecutionReportEvent(message));
				break;

			case MsgType.ORDER_CANCEL_REJECT:
				_fixMessageIf.observableOrderCancel(new OrderCancelEvent(message));
				break;

			case MsgType.BUSINESS_MESSAGE_REJECT:
				_fixMessageIf.observableBusinessMessageReject(new BusinessMessageEvent(message));
				break;
				
			case MsgType.REJECT:
				_fixMessageIf.observableReject(new RejectEvent(message));
				break;
			}
			_fixMessageIf.observableMessage(message);
		}

	}

	/**
	 * Listen for events
	 * 
	 * @param handler
	 * @throws NullPointerException
	 */
	public void registerHandleEvents(HandleEventIf handler) {
		_fixMessageIf = handler;
	}

	/**
	 * Send custom fix message
	 * @param message
	 * @throws NullPointerException
	 * @throws NotConnectedException
	 */
	@Override
	public void sendFixMessage(Message message) throws NullPointerException, NotConnectedException {
		_fixConnection.sendMessage(message);
	}
}
