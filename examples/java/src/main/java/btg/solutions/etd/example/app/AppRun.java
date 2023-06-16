package btg.solutions.etd.example.app;

import java.math.BigDecimal;
import java.util.UUID;

import com.btgpactualsolutions.etd.BtgSolutionsConnectionIf;
import com.btgpactualsolutions.etd.BtgSolutionsConnectionImpl;
import com.btgpactualsolutions.etd.HandleEventIf;
import com.btgpactualsolutions.etd.config.ConnectionTimeOutException;
import com.btgpactualsolutions.etd.config.FixSettings;
import com.btgpactualsolutions.etd.config.NotConnectedException;
import com.btgpactualsolutions.etd.events.BusinessMessageEvent;
import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import com.btgpactualsolutions.etd.events.OrderCancelEvent;
import com.btgpactualsolutions.etd.events.RejectEvent;
import com.btgpactualsolutions.etd.model.CreateNewOrderSingle;
import com.btgpactualsolutions.etd.model.CreateOrderCancel;
import com.btgpactualsolutions.etd.model.CreateOrderReplace;

import quickfix.ConfigError;
import quickfix.Message;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

public class AppRun implements HandleEventIf {
	private static String TRADER_EXAMPLE = "changeHere@btgpactual.com";
	private static String CLIENTID = "CLIENTID";
	private Object _monitor = new Object();
	private OrderManager _orderManager;
	private BtgSolutionsConnectionIf _connection;
	private UUID _orderIdExample;

	public static void main(String[] args) throws Exception {
		new AppRun().Run();
	}

	/*
	 * Connection BTG Solutions
	 */
	public void Run() throws ConnectionTimeOutException, NotConnectedException, ConfigError, InterruptedException {
		_orderManager = new OrderManager();
		var settings = new FixSettings();

		String socketConnectionHost = "host";
		int socktetConnectionPort = 0;

		String username = "";
		String password = "";
		String senderCompId = "";
		String targetCompId = "";
		String sslCertificate = "cert.pem";
		String dialect = "config\\FIX44-B3.xml";
		String defaultClientId = "CLIENTID";
		settings.setDialect(dialect);
		_connection = new BtgSolutionsConnectionImpl(senderCompId, targetCompId, socketConnectionHost,
				socktetConnectionPort, sslCertificate, username, password, defaultClientId, settings);
		_connection.registerHandleEvents(this);
		_connection.start();
		executeDemonstration(true, false);
		_connection.stop();
	}

	private void executeDemonstration(boolean dmaExample, boolean cancelExample)
			throws NotConnectedException, InterruptedException {
		String clOrdId = UUID.randomUUID().toString().replace("-", "");
		synchronized (_monitor) {
			if (dmaExample) {
				System.out.println("Sending New Single Order (DMA)\n\n");
				_orderIdExample = newOrderSingleDMA(clOrdId);
			} else {
				System.out.println("Sending New Single Order (Care)\n\n");
				_orderIdExample = newOrderSingle(clOrdId);
			}
			_monitor.wait();

			System.out.println("Sending Order Replace 1500 quantity, 36.22 price\n\n");
			orderReplace(1500, BigDecimal.valueOf(36.22));
			_monitor.wait();

			System.out.println("Sending Order Replace 2500 quantity, 50.22 price\n\n");
			orderReplace(2500, BigDecimal.valueOf(50.22));
			_monitor.wait();

			System.out.println("Sending Order Replace 3500 quantity, 51.44 price\n\n");
			orderReplace(3500, BigDecimal.valueOf(51.44));
			_monitor.wait();

			if (cancelExample) {
				System.out.println("Waiting 10 seconds to cancel the order");
				Thread.sleep(10000);

				System.out.println("Sending Order Cancel");
				orderCancel();
			} else {
				System.out.println("Sending Order Replace 1000 quantity, MARKET price\n\n");
				orderReplace(1000, null);
			}
			_monitor.wait();
		}
	}

	/*
	 * Orders Methods
	 */
	private UUID newOrderSingleDMA(String clOrdId) throws NotConnectedException, NullPointerException {
		UUID uniqueId = _orderManager.register(clOrdId);
		var model = new CreateNewOrderSingle();

		model.setClOrdID(clOrdId);
		model.setOrderQty(BigDecimal.valueOf(500));
		model.setSymbol("PETR3");
		model.setClientId(CLIENTID);
		model.setTrader(TRADER_EXAMPLE);
		model.setSide(new Side(Side.BUY));
		model.setDMA(true);
		model.setAccount(114);
		model.setExecBroker(935);
		model.setCountry("BR");
		model.setPrice(new BigDecimal(1));

		_connection.newOrderSingle(model);
		return uniqueId;
	}

	private UUID newOrderSingle(String clOrdId) throws NotConnectedException, NullPointerException {
		UUID uniqueId = _orderManager.register(clOrdId);
		var model = new CreateNewOrderSingle();

		model.setClOrdID(clOrdId);
		model.setOrderQty(BigDecimal.valueOf(500));
		model.setSymbol("TOTS3");
		model.setClientId(CLIENTID);
		model.setTrader(TRADER_EXAMPLE);
		model.setSide(new Side(Side.BUY));
		model.setOrdType(new OrdType(OrdType.LIMIT));
		model.setDMA(false);
		model.setTimeInForce(new TimeInForce(TimeInForce.DAY));
		_connection.newOrderSingle(model);
		return uniqueId;
	}

	private void orderReplace(Integer quantity, BigDecimal price) throws NotConnectedException, NullPointerException {
		ExecutionReportEvent lastExecutionReport = _orderManager.tryGetLastExecutionReport(_orderIdExample);
		CreateOrderReplace model = new CreateOrderReplace();
		model.setOrigClOrdID(lastExecutionReport.getClOrdID());
		model.setSide(lastExecutionReport.getSide());

		if (quantity != null) {
			model.setOrderQty(quantity);
		}

		if (price != null) {
			model.setPrice(price);
			model.setOrdType(new OrdType(OrdType.LIMIT));
		} else {
			model.setOrdType(new OrdType(OrdType.MARKET));
		}
		_connection.orderReplaceRequest(model);
	}

	private void orderCancel() throws NotConnectedException, NullPointerException {
		ExecutionReportEvent lastExecutionReport = _orderManager.tryGetLastExecutionReport(_orderIdExample);

		var model = new CreateOrderCancel();
		model.setOrigClOrdID(lastExecutionReport.getClOrdID());
		model.setSide(lastExecutionReport.getSide());
		model.setOrderID(lastExecutionReport.getOrderID());

		// if Care, quantity does not have value, and is not required
		if (lastExecutionReport.getOrderQty() != null) {
			// is required if ordType is DMA, and the property OrderQty will have value
			model.setOrderQty(lastExecutionReport.getOrderQty());
		}
		_connection.orderCancelRequest(model);
	}

	/*
	 * Observable Event Messages
	 */

	@Override
	public void observableBusinessMessageReject(BusinessMessageEvent arg0) {
		System.out.println("handlerBusinessMessageEvent: " + arg0.toString());

	}

	@Override
	public void observableExecutionReport(ExecutionReportEvent message) {
		UUID uniqueId = _orderManager.update(message);
		if (_orderIdExample.equals(uniqueId)) {
			Lighthouse();
		}

		System.out.println("handleExecutionReport: " + message.toString());
	}

	@Override
	public void observableIsSessionAlive(boolean arg0) {
		System.out.println("handlerIsSessionAliveEvent: " + arg0);

	}

	@Override
	public void observableMessage(Message arg0) {
		System.out.println("handleMessage: " + arg0.toString());

	}

	@Override
	public void observableOrderCancel(OrderCancelEvent arg0) {
		System.out.println("handleOrderCancel: " + arg0.toString());

	}

	@Override
	public void observableReject(RejectEvent arg0) {
		System.out.println("handleOrderRejected: " + arg0.toString());
	}

	private void Lighthouse() {
		synchronized (_monitor) {
			_monitor.notifyAll();
		}
	}
}
