package com.btgpactualsolutions.etd.core;

import java.time.Duration;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.btgpactualsolutions.etd.config.FixSettings;
import com.btgpactualsolutions.etd.config.ConnectionTimeOutException;
import com.btgpactualsolutions.etd.config.InitiatorSettings;
import com.btgpactualsolutions.etd.config.NotConnectedException;

import quickfix.CachedFileStoreFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.Dictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.RuntimeError;
import quickfix.Session;
import quickfix.SessionFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.StringField;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Username;
import quickfix.mina.ssl.SSLSupport;

public class BtgSolutionsFixConnectionImpl implements BtgSolutionsFixConnectionIf {
	private static final Logger log = LoggerFactory.getLogger(BtgSolutionsFixConnectionImpl.class);

	private final HandlerMessageIf _handler;
	private final Initiator _initiator;
	private final String _username;
	private final String _password;

	private Object _monitor = new Object();
	private boolean _isConnect = false;
	private InitiatorSettings _settings;
	private Session defaultSession;

	/**
	 * 
	 * @param handler
	 * @param username
	 * @param password
	 * @param senderCompID
	 * @param targetCompID
	 * @param socketConnectHost
	 * @param socketConnectPort
	 * @param SSLCertificate
	 * @param settings
	 * @throws ConfigError
	 */
	public BtgSolutionsFixConnectionImpl(HandlerMessageIf handler, String username, String password,
			String senderCompID, String targetCompID, String socketConnectHost, int socketConnectPort,
			String SSLCertificate, FixSettings settings) throws ConfigError {

		_handler = handler;
		_username = username;
		_password = password;

		SessionSettings setSettings = SetSettings(senderCompID, targetCompID, socketConnectHost, socketConnectPort,
				SSLCertificate, settings);

		_initiator = new SocketInitiator(this, new CachedFileStoreFactory(setSettings), setSettings,
				new DefaultMessageFactory());
	}

	public void onCreate(SessionID sessionId) {
		log.info("onCreate" + sessionId);
	}

	public void onLogon(SessionID sessionId) {
		log.info("onLogon" + sessionId);
		loadDefaultSession();
		handlerSession(true);
	}

	public void onLogout(SessionID sessionId) {
		log.info("onLogout" + sessionId);
		handlerSession(false);
	}

	public void toAdmin(Message message, SessionID sessionId) {
		try {
			log.info("toAdmin Message:" + sessionId + "| SessionID:" + sessionId);

			if (MsgType.LOGON.equals(message.getHeader().getString(MsgType.FIELD))) {
				if (!((_username == null || _username.isBlank()) && (_password == null || _password.isBlank()))) {
					message.setField(new StringField(Username.FIELD, _username));
					message.setField(new StringField(Password.FIELD, _password));
				}
			}

			_handler.handleMessage(message);
		} catch (FieldNotFound e) {
			e.printStackTrace();
			log.error("toAdmin", e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("toAdmin", e);
		}
	}

	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("fromAdmin Message:" + sessionId + "| SessionID:" + sessionId);
	}

	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		log.info("fromAdmin Message:" + sessionId + "| SessionID:" + sessionId);
		try {
			_handler.handleMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("toApp", e);
		}
	}

	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		try {
			_handler.handleMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("fromApp", e);
		}
	}

	private void loadDefaultSession() {
		ArrayList<SessionID> sessionList = _initiator.getSessions();
		SessionID targetSessionID = null;

		for (SessionID sessionID : sessionList) {
			if (sessionID.getBeginString().equals(_settings.getFixSettings().getBeginString())
					&& sessionID.getTargetCompID().equals(_settings.getTargetCompID())
					&& sessionID.getSenderCompID().equals(_settings.getSenderCompID())) {
				targetSessionID = sessionID;
				break;
			}
		}

		defaultSession = Session.lookupSession(targetSessionID);
	}

	@Override
	public void start() throws ConnectionTimeOutException, NotConnectedException {
		try {
			log.info("Init connected Initiador");

			InitiatorCannotBeNull();

			_initiator.start();

			synchronized (_monitor) {
				log.info("Connecting...");

				_monitor.wait(Duration.ofSeconds(30).toMillis());

				if (_isConnect) {
					log.info("Success connected initiador");
				} else {
					log.info("fail connected initiador");
					throw new ConnectionTimeOutException();
				}
			}
			return;
		} catch (RuntimeError e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (ConfigError e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.error("Error in start initiator");
	}

	@Override
	public void stop() {
		log.info("Init disconnected Initiador");

		if (_initiator != null) {
			_initiator.stop();
		}

		log.info("Success disconnected Initiador");
	}

	public void sendMessage(Message msg) throws NotConnectedException, NullPointerException {
		log.info("send message");
		if (!_isConnect) {
			throw new NotConnectedException();
		}

		if (msg == null) {
			throw new NullPointerException("Message is null");
		}

		defaultSession.send(msg);
		log.info("Success send message: " + msg.toString());
	}

	@Override
	public boolean getIsConnect() {
		return _isConnect;
	}

	private void InitiatorCannotBeNull() throws NotConnectedException {
		if (_initiator == null) {
			throw new NotConnectedException();
		}
	}

	private SessionSettings SetSettings(String senderCompID, String targetCompID, String socketConnectHost,
			int socketConnectPort, String SSLCertificate, FixSettings settingsParametter) throws ConfigError {
		log.info("Init configuration Settings");
		_settings = new InitiatorSettings();
		_settings.setAppSettings(settingsParametter);
		_settings.setSenderCompID(senderCompID);
		_settings.setTargetCompID(targetCompID);
		_settings.setSocketConnectHost(socketConnectHost);
		_settings.setSocketConnectPort(socketConnectPort);
		_settings.setSSLCertificate(SSLCertificate);

		FixSettings fixSettings = _settings.getFixSettings();
		SessionSettings settings = setupSessionSettings(fixSettings);

		Dictionary sessionDic = new Dictionary();
		SessionID sessionId = new SessionID(fixSettings.getBeginString(), _settings.getSenderCompID(),
				_settings.getTargetCompID());

		sessionDic.setString(SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.INITIATOR_CONNECTION_TYPE);
		sessionDic.setString(SessionSettings.BEGINSTRING, fixSettings.getBeginString());
		sessionDic.setString(SessionSettings.SENDERCOMPID, _settings.getSenderCompID());
		sessionDic.setString(SessionSettings.TARGETCOMPID, _settings.getTargetCompID());
		setupDialect(sessionDic);
		sessionDic.setString(Initiator.SETTING_SOCKET_CONNECT_HOST, _settings.getSocketConnectHost());
		sessionDic.setString(Initiator.SETTING_SOCKET_CONNECT_PORT, String.valueOf(_settings.getSocketConnectPort()));
		setupSsl(sessionDic);

		settings.set(sessionId, sessionDic);

		log.info("End configuration Settings");
		return settings;
	}

	private void setupDialect(Dictionary sessionDictionary) {
		FixSettings fixSettings = _settings.getFixSettings();
		String dialect = fixSettings.getDialect();
		if (dialect == null || dialect.isEmpty()) {
			sessionDictionary.setString(Session.SETTING_USE_DATA_DICTIONARY, "N");
		} else {
			sessionDictionary.setString(Session.SETTING_USE_DATA_DICTIONARY, "Y");
			sessionDictionary.setString(Session.SETTING_DATA_DICTIONARY, fixSettings.getDialect());
		}
	}

	private void setupSsl(Dictionary sessionDic) {
		FixSettings fixSettings = _settings.getFixSettings();
		if (_settings.getSSLCertificate() == null || _settings.getSSLCertificate().isEmpty()) {
			sessionDic.setString(SSLSupport.SETTING_USE_SSL, "N");
		} else {
			sessionDic.setString("SSLCertificate", _settings.getSSLCertificate());
			sessionDic.setString(SSLSupport.SETTING_USE_SSL, String.valueOf(fixSettings.getSSLEnable()));
			sessionDic.setString("SSLValidateCertificates", String.valueOf(fixSettings.getSSLValidateCertificates()));
		}
	}

	private SessionSettings setupSessionSettings(FixSettings fixSettings) throws ConfigError {
		SessionSettings settings = new SessionSettings();

		Dictionary sessionDictionary = new Dictionary();
		sessionDictionary.setLong(Initiator.SETTING_RECONNECT_INTERVAL, fixSettings.getReconnectInterval());
		sessionDictionary.setString(CachedFileStoreFactory.SETTING_FILE_STORE_PATH, fixSettings.getFileStorePath());
		sessionDictionary.setString(Session.SETTING_START_TIME, String.valueOf(fixSettings.getStartTime()));
		sessionDictionary.setString(Session.SETTING_END_TIME, String.valueOf(fixSettings.getEndTime()));
		sessionDictionary.setString(Session.SETTING_HEARTBTINT, String.valueOf(fixSettings.getHeartBtInt()));
		sessionDictionary.setLong(Session.SETTING_LOGOUT_TIMEOUT, fixSettings.getLogoutTimeout());
		sessionDictionary.setString(Session.SETTING_RESET_ON_LOGON, String.valueOf(fixSettings.getResetOnLogon()));
		sessionDictionary.setString(Session.SETTING_RESET_ON_LOGOUT, String.valueOf(fixSettings.getResetOnLogout()));
		sessionDictionary.setString(Session.SETTING_RESET_ON_DISCONNECT,
				String.valueOf(fixSettings.getResetOnDisconnect()));
		sessionDictionary.setString(Session.SETTING_VALIDATE_USER_DEFINED_FIELDS, "N");
		sessionDictionary.setString(Session.SETTING_VALIDATE_FIELDS_HAVE_VALUES, "N");
		sessionDictionary.setString(Session.SETTING_VALIDATE_FIELDS_OUT_OF_ORDER, "N");
		sessionDictionary.setString(Session.SETTING_ALLOW_UNKNOWN_MSG_FIELDS, "Y");

		settings.set(sessionDictionary);

		return settings;
	}

	private void handlerSession(boolean activated) {
		_isConnect = activated;
		if (_isConnect) {
			synchronized (_monitor) {
				_monitor.notifyAll();
			}
		}
	}
}
