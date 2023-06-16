package com.btgpactualsolutions.etd.config;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FixSettings {
	private int reconnectInterval = 10;
	private String fileStorePath = "Store";
	private LocalTime startTime = LocalTime.parse("00:00:01", DateTimeFormatter.ofPattern("HH:mm:ss"));
	private LocalTime endTime = LocalTime.parse("23:59:59", DateTimeFormatter.ofPattern("HH:mm:ss"));
	private int heartBtInt = 30;
	private int logoutTimeout = 5;
	private char resetOnLogon = 'N';
	private char resetOnLogout = 'N';
	private char resetOnDisconnect = 'N';
	private String beginString = "FIX.4.4";
	private String dialect;

	private char sSLEnable = 'Y';
	private char sSLValidateCertificates = 'N';

	public int getReconnectInterval() {
		return this.reconnectInterval;
	}

	public void setReconnectInterval(int reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public String getFileStorePath() {
		return this.fileStorePath;
	}

	public void setFileStorePath(String fileStorePath) {
		this.fileStorePath = fileStorePath;
	}

	public LocalTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getHeartBtInt() {
		return this.heartBtInt;
	}

	public void setHeartBtInt(int heartBtInt) {
		this.heartBtInt = heartBtInt;
	}

	public int getLogoutTimeout() {
		return this.logoutTimeout;
	}

	public void setLogoutTimeout(int logoutTimeout) {
		this.logoutTimeout = logoutTimeout;
	}

	public char getResetOnLogon() {
		return this.resetOnLogon;
	}

	public void setResetOnLogon(char resetOnLogon) {
		this.resetOnLogon = resetOnLogon;
	}

	public char getResetOnLogout() {
		return this.resetOnLogout;
	}

	public void setResetOnLogout(char resetOnLogout) {
		this.resetOnLogout = resetOnLogout;
	}

	public char getResetOnDisconnect() {
		return this.resetOnDisconnect;
	}

	public void setResetOnDisconnect(char resetOnDisconnect) {
		this.resetOnDisconnect = resetOnDisconnect;
	}

	public String getBeginString() {
		return this.beginString;
	}

	public void setBeginString(String beginString) {
		this.beginString = beginString;
	}

	public String getDialect() {
		return this.dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public char getSSLEnable() {
		return this.sSLEnable;
	}

	public void setSSLEnable(char sSLEnable) {
		this.sSLEnable = sSLEnable;
	}

	public char getSSLValidateCertificates() {
		return this.sSLValidateCertificates;
	}

	public void setSSLValidateCertificates(char sSLValidateCertificates) {
		this.sSLValidateCertificates = sSLValidateCertificates;
	}
}