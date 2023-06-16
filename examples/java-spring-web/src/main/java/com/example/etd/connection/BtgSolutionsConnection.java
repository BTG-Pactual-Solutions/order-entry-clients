package com.example.etd.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.btgpactualsolutions.etd.BtgSolutionsConnectionIf;
import com.btgpactualsolutions.etd.BtgSolutionsConnectionImpl;
import com.btgpactualsolutions.etd.HandleEventIf;
import com.btgpactualsolutions.etd.config.FixSettings;
import com.btgpactualsolutions.etd.config.NotConnectedException;
import com.btgpactualsolutions.etd.events.BusinessMessageEvent;
import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import com.btgpactualsolutions.etd.events.OrderCancelEvent;
import com.btgpactualsolutions.etd.events.RejectEvent;
import com.btgpactualsolutions.etd.model.CreateNewOrderSingle;
import com.example.etd.manager.OrderManager;

import quickfix.Message;

@Service
public class BtgSolutionsConnection implements HandleEventIf {

	private BtgSolutionsConnectionIf _connection;
	
	@Autowired
	private OrderManager orderManager;

	@Value("${solutions.order.entry.host}")
	private String connectionHost;

	@Value("${solutions.order.entry.port}")
	private int connectionPort;

	@Value("${solutions.order.entry.senderCompId}")
	private String senderCompId;

	@Value("${solutions.order.entry.targetCompId}")
	private String targetCompId;

	@Value("${solutions.order.entry.username}")
	private String username;

	@Value("${solutions.order.entry.password}")
	private String password;

	@Value("${solutions.order.entry.sslFile}")
	private String sslFile;

	@Value("${solutions.order.entry.dialect}")
	private String dialect;
	
	@Value("${solutions.order.entry.defaultClientId}")
	private String defaultClientId;

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		var settings = new FixSettings();
		settings.setDialect(dialect);
		settings.setReconnectInterval(5);
		
		
		try {
			_connection = new BtgSolutionsConnectionImpl(senderCompId, targetCompId, connectionHost, connectionPort,
					sslFile, username, password, defaultClientId, settings);

			_connection.registerHandleEvents(this);
			_connection.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void observableBusinessMessageReject(BusinessMessageEvent arg0) {
	}

	@Override
	public void observableExecutionReport(ExecutionReportEvent message) {
		orderManager.update(message);
	}

	@Override
	public void observableIsSessionAlive(boolean sessionIsAlive) {
	}

	@Override
	public void observableMessage(Message arg0) {
	}

	@Override
	public void observableOrderCancel(OrderCancelEvent arg0) {
	}

	@Override
	public void observableReject(RejectEvent arg0) {
	}

	public void send(CreateNewOrderSingle newOrderSingle) throws NullPointerException, NotConnectedException {
		_connection.newOrderSingle(newOrderSingle);
	}
}
