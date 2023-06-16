package com.example.etd.api.rest;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.btgpactualsolutions.etd.config.NotConnectedException;
import com.btgpactualsolutions.etd.events.ExecutionReportEvent;
import com.btgpactualsolutions.etd.model.CreateNewOrderSingle;
import com.example.etd.connection.BtgSolutionsConnection;
import com.example.etd.manager.OrderManager;

@RestController
public class OrderControllerRest {

	@Autowired
	private BtgSolutionsConnection btgSolutionsConnection;
	
	@Autowired
	private OrderManager orderManager;
	
	@GetMapping
	public HashMap<UUID, ExecutionReportEvent> getAll() {
		return orderManager.getAll();
	}
	
	@PostMapping("new")
	public void newOrderSingle(@RequestBody CreateNewOrderSingle newOrderSingle) throws NullPointerException, NotConnectedException {
		btgSolutionsConnection.send(newOrderSingle);
	}
}
