package com.btgpactualsolutions.etd.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

public class CreateOrderReplace {
	private String clientId;
	private String clOrdID;
	private String origClOrdID;

	private Integer orderQty;
	private BigDecimal price;
	private String symbol;
	private String currency;
	private String country;
	private LocalDateTime transactTime;
	private String handlInst;

	private BigDecimal minQty;
	private BigDecimal displayQty;
	private BigDecimal stopPx;
	private String exDestination;

	private String securityId;
	private String securityIdSource;

	private Side side = null;
	private OrdType ordType = null;
	private TimeInForce timeInForce = null;

	private List<String> validations;

	public CreateOrderReplace() {
		this.validations = new ArrayList<String>(10);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
	}

	public String getOrigClOrdID() {
		return origClOrdID;
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public OrdType getOrdType() {
		return ordType;
	}

	public void setOrdType(OrdType ordType) {
		this.ordType = ordType;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public TimeInForce getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public LocalDateTime getTransactTime() {
		return transactTime;
	}

	public void setTransactTime(LocalDateTime transactTime) {
		this.transactTime = transactTime;
	}

	public String getHandlInst() {
		return handlInst;
	}

	public void setHandlInst(String handlInst) {
		this.handlInst = handlInst;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public BigDecimal getDisplayQty() {
		return displayQty;
	}

	public void setDisplayQty(BigDecimal displayQty) {
		this.displayQty = displayQty;
	}

	public BigDecimal getStopPx() {
		return stopPx;
	}

	public void setStopPx(BigDecimal stopPx) {
		this.stopPx = stopPx;
	}

	public String getExDestination() {
		return exDestination;
	}

	public void setExDestination(String exDestination) {
		this.exDestination = exDestination;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getSecurityIdSource() {
		return securityIdSource;
	}

	public void setSecurityIdSource(String securityIdSource) {
		this.securityIdSource = securityIdSource;
	}

	public EvaluateResult evaluate() {
		this.validations.clear();
		if (this.getClientId() == null || this.getClientId().isEmpty()) {
			this.validations.add("ClientId is required");
		}

		if (getOrigClOrdID() == null || getOrigClOrdID().isEmpty()) {
			this.validations.add("OrigClOrdID is required");
		}

		if (this.getSide() == null) {
			this.validations.add("Side is required");
		}

		if (this.getOrderQty() == null) {
			this.validations.add("OrderQty is required");
		}

		// Validation to avoid unintentional order Market
		if (this.getOrdType() == null && this.getPrice() == null) {
			this.validations.add(
					"Price or OrdType is required, This is a validation to avoid unintentional Market order, if that is your intention, please set ordType as Market!");
		}

		if (this.getOrdType() != null) {
			if (OrdType.LIMIT == this.getOrdType().getValue() && this.getPrice() == null) {
				this.validations.add("Price is required if OrdType is Limit");
			} else if (OrdType.MARKET == this.getOrdType().getValue() && this.getPrice() != null) {
				this.validations.add("Price should be null if OrdType is Market");
			}
		}
		return new EvaluateResult(String.join(", ", validations));
	}
}
