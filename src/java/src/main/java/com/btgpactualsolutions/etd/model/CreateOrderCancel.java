package com.btgpactualsolutions.etd.model;

import quickfix.field.Side;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderCancel {
	private String clientId;
	private String ClOrdID;
	private String OrigClOrdID;
	private String OrderID;
	private BigDecimal orderQty;
	private Side Side;

	private List<String> validations;

	public CreateOrderCancel() {
		this.validations = new ArrayList<>(10);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClOrdID() {
		return ClOrdID;
	}

	public void setClOrdID(String clOrdID) {
		ClOrdID = clOrdID;
	}

	public String getOrigClOrdID() {
		return OrigClOrdID;
	}

	public void setOrigClOrdID(String origClOrdID) {
		OrigClOrdID = origClOrdID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public Side getSide() {
		return Side;
	}

	public void setSide(Side side) {
		Side = side;
	}

	public EvaluateResult evaluate() {
		this.validations.clear();
		if (this.getClientId() == null || this.getClientId().isEmpty()) {
			this.validations.add("ClientId is required");
		}
		if ((getOrigClOrdID() == null || getOrigClOrdID().isEmpty())
				&& (getOrderID() == null || getOrderID().isEmpty())) {
			this.validations.add("OrderID or OrigClOrdID is required");
		}

		if (this.getSide() == null) {
			this.validations.add("Side is required");
		}
		return new EvaluateResult(String.join(", ", validations));
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}
}
