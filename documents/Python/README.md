# BTG Solutions Trade API package

BTG solutions trade api package.


## Usage

Get your token and use it in the examples below.

> Instantiate an order controller. Provide your token, account number, execution broker and entity to start sending orders.
sampleInterval is optional

```Python
controller = OrderController(
    token="TOKEN",
    order_api_host="URL",
    account="YOUR_ACCOUNT_NUMBER",
    execBroker="YOUR_EXEC_BROKER",
    entity="YOUR_ENTITY",
    sampleInterval = None
)
```

> One can provide a custom order update callback function.

```Python
def order_update_callback(order):
    print(f"Order update: {order}")

controller = OrderController(
    token=token,
    order_api_host="URL",
    account="YOUR_ACCOUNT_NUMBER",
    execBroker="YOUR_EXEC_BROKER",
    entity="YOUR_ENTITY",
    order_update_callback=order_update_callback,
    sampleInterval = 5
    )
```

> Create an order and receive the resulting order ID.

```Python
orderId = controller.create_order(
    symbol="PETR4",
    side="S",
    qty="5000",
    price="20.41",
    timeInForce="Day",
    isDMA="true"
)
```

> Create an order and receive the resulting order ID.
	This signature allow user to use different account, entity and broker of Controller session 

```Python
orderId = controller.create_order(
    symbol="PETR4",
    side="S",
    qty="5000",
    price="20.41",
    timeInForce="Day",
    isDMA="true",
    account="ACCOUNT", 
    execBroker="NUMBER_OF_BROKER", 
    entity="ENTITY",
    memo = "memo"
)
```


> Create an order using strategy and receive the resulting order ID.
	This signature allow user to use different account, entity and broker of Controller session 

```Python
strategyParameter = { 
"max-floor": "100",    
"start-time": "20240614-19:00:00",
"end-time": "20240614-20:00:00"
}

orderId = controller.create_order(
    symbol="PETR4",
    side="S",
    qty="5000",
    price="20.41",
    timeInForce="Day",
    isDMA="true",
    account="ACCOUNT", 
    execBroker="NUMBER_OF_BROKER", 
    entity="ENTITY",
    strategy = "twap",
    strategyParameter = strategyParameter
)
```

> Change order.

```Python
controller.change_order(
    id="YOUR_ORDER_ID",
    qty="5000",
    price="20.43",
    timeInForce="Day"
)
```

> Cancel order.

```Python
controller.cancel_order(
    id="YOUR_ORDER_ID",
)
```

> Cancel ALL order.

```Python
controller.cancel_all_orders()
```

> Get Trades.

```Python
controller.get_trades()
```

> Get Orders.

```Python
controller.get_orders()
```

> Get Orders by Params.
All params are optional
```Python
controller.get_orders(complete = True, symbol = "WEGE3", status = "New", side = "Sell", memo = "memo", parent = True)
```

> Get Orders.

```Python
controller.get_order(
		id = "ORDER ID"
)
```

> Get a summary of all your orders.

```Python
controller.summary()
```

## Support

Get help at support@btgpactualsolutions.com
