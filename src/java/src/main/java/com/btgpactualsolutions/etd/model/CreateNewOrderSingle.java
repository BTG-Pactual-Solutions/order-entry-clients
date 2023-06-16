package com.btgpactualsolutions.etd.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

public class CreateNewOrderSingle {

	private Integer account;
	private Integer execBroker;
	private String clOrdID;

	private boolean isDMA;

	private String clientId;
	private BigDecimal price;
	private String symbol;

	private String exDestination;
	private String securityId;
	private String securityIdSource;

	private String currency;
	private String country;

	private BigDecimal orderQty;
	private BigDecimal minQty;
	private BigDecimal displayQty;
	private BigDecimal stopPx;

	private String trader;

	private Side side;
	private OrdType ordType;
	private TimeInForce timeInForce;
	private List<String> validations;

	public CreateNewOrderSingle() {
		this.validations = new ArrayList<String>(10);
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public Integer getExecBroker() {
		return execBroker;
	}

	public void setExecBroker(int execBroker) {
		this.execBroker = execBroker;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
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

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public void setDisplayQty(BigDecimal displayQty) {
		this.displayQty = displayQty;
	}

	public BigDecimal getDisplayQty() {
		return this.displayQty;
	}

	public void setStopPx(BigDecimal stopPx) {
		this.stopPx = stopPx;
	}

	public BigDecimal getStopPx() {
		return this.stopPx;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isDMA() {
		return isDMA;
	}

	public void setDMA(boolean isDMA) {
		this.isDMA = isDMA;
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

	public String getExDestination() {
		return this.exDestination;
	}

	public void setExDestination(String exDestination) {
		this.exDestination = exDestination;
	}

	public String getSecurityId() {
		return this.securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getSecurityIdSource() {
		return this.securityIdSource;
	}

	public void setSecurityIdSource(String securityIdSource) {
		this.securityIdSource = securityIdSource;
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

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public EvaluateResult evaluate() {
		this.validations.clear();
		if (this.getClientId() == null || this.getClientId().isEmpty()) {
			this.validations.add("ClientId is required");
		}

		if (this.getSide() == null) {
			this.validations.add("Side is required");
		}

		if (this.getOrderQty() == null) {
			this.validations.add("OrderQty is required");
		}

		if (this.getTrader() == null || this.getTrader().isBlank()) {
			this.validations.add("Trader is required");
		}

		if (this.getSymbol() == null || this.getSymbol().isEmpty()) {
			this.validations.add("Symbol is required");
		}

		if (this.isDMA()) {
			if (this.getAccount() == null) {
				this.validations.add("Account is required since is DMA");
			}
			if (this.getExecBroker() == null) {
				this.validations.add("ExecBroker is required since is DMA");
			}
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
