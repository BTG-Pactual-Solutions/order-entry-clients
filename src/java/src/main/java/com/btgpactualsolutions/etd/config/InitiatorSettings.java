package com.btgpactualsolutions.etd.config;

public class InitiatorSettings {
	private String senderCompID;
	private String targetCompID;
	private String socketConnectHost;
	private long socketConnectPort;
	private String sslCertificate;	
	private FixSettings fixSettings;

	public InitiatorSettings() {
		this.fixSettings = new FixSettings();
	}
	
    public String getSenderCompID() {
        return senderCompID;
    }

    public void setSenderCompID(String senderCompID) {
        this.senderCompID = senderCompID;
    }

    public String getTargetCompID() {
        return targetCompID;
    }

    public void setTargetCompID(String targetCompID) {
        this.targetCompID = targetCompID;
    }

    public String getSocketConnectHost() {
        return socketConnectHost;
    }

    public void setSocketConnectHost(String socketConnectHost) {
        this.socketConnectHost = socketConnectHost;
    }

    public long getSocketConnectPort() {
        return socketConnectPort;
    }

    public void setSocketConnectPort(long socketConnectPort) {
        this.socketConnectPort = socketConnectPort;
    }

    public String getSSLCertificate() {
        return sslCertificate;
    }

    public void setSSLCertificate(String sslCertificate) {
        this.sslCertificate = sslCertificate;
    }

    public FixSettings getFixSettings() {
    	return fixSettings;
    }
    
    public void setAppSettings(FixSettings app) {
        app = app != null ? app : new FixSettings();

        fixSettings.setReconnectInterval(app.getReconnectInterval());        
        fixSettings.setFileStorePath(app.getFileStorePath());
        fixSettings.setStartTime(app.getStartTime());
        fixSettings.setEndTime(app.getEndTime());
        fixSettings.setHeartBtInt(app.getHeartBtInt());
        fixSettings.setLogoutTimeout(app.getLogoutTimeout());
        fixSettings.setResetOnLogon(app.getResetOnLogon());
        fixSettings.setResetOnLogout(app.getResetOnLogout());
        fixSettings.setResetOnDisconnect(app.getResetOnDisconnect());        
        fixSettings.setBeginString(app.getBeginString());
        fixSettings.setDialect(app.getDialect());
        fixSettings.setSSLEnable(app.getSSLEnable());
        fixSettings.setSSLValidateCertificates(app.getSSLValidateCertificates());
    }
}

