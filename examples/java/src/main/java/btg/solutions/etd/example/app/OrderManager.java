package btg.solutions.etd.example.app;

import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import quickfix.field.ExecType;

import java.util.HashMap;
import java.util.UUID;

public class OrderManager {
	private HashMap<String, UUID> _uniqueIdByClOrdId;
	private HashMap<UUID, ExecutionReportEvent> _executionReportByUniqueId;

	public OrderManager() {
		_uniqueIdByClOrdId = new HashMap<>();
		_executionReportByUniqueId = new HashMap<>();
	}

	public UUID register(String clOrdId) {
		UUID uniqueId = UUID.randomUUID();
		_uniqueIdByClOrdId.put(clOrdId, uniqueId);
		return uniqueId;
	}

	public ExecutionReportEvent tryGetLastExecutionReport(UUID uniqueId) {
		return _executionReportByUniqueId.getOrDefault(uniqueId, null);
	}

	public UUID update(ExecutionReportEvent message) {
		UUID uniqueId = _uniqueIdByClOrdId.getOrDefault(message.getClOrdID(), null);
		if (uniqueId == null) {
			uniqueId = getByOriginClOrdIdAndUpdate(message);
		}
		if (message.getExecType() == null || ExecType.REJECTED == message.getExecType().toCharArray()[0]) {
			return uniqueId;
		}
		if (uniqueId == null) {
			uniqueId = register(message.getClOrdID());
		}
		_executionReportByUniqueId.put(uniqueId, message);
		return uniqueId;
	}

	private UUID getByOriginClOrdIdAndUpdate(ExecutionReportEvent message) {
		UUID uniqueId = _uniqueIdByClOrdId.getOrDefault(message.getOrigClOrdID(), null);
		if (uniqueId != null) {
			_uniqueIdByClOrdId.remove(message.getOrigClOrdID());
			_uniqueIdByClOrdId.put(message.getClOrdID(), uniqueId);
		}
		return uniqueId;
	}

	public HashMap<UUID, ExecutionReportEvent> getAll() {
		return _executionReportByUniqueId;
	}
}