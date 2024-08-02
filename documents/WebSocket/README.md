# API WebSocket
 
Web Socket API documentation to integrate with Solutions Order Entry.

## Summary
- [1. Authentication](#1-authentication)
- [2. Observation](#2-observation)
- [3. Subscribe](#3-subscribe)
- [4. Events](#4-events)

## 1. Authentication

It is necessary to provide a Bearer authorization token in the header when connecting to websocket api. In this documentation, you may notice that a token
is required when seeing the following character set: `{token}`. Consider replacing
this field with your own authorization token.

## 2. Observation
This document describes how to submit and manage orders for the onshore market
(Brazil), for offshore integration additional fields are required for instrument identification.
 
On the library or tool that you are using to connect, it is necessary to set the following headers
| Header | Value | Obs |
| --- | --- | --- |
| **Authorization** | {token} | required |
| **Accept-Encoding** | utf8-json/json | for events scaped on a json format |
| **clientType** | Main | required |
| **User-Agent** | {generic value} | required |

## 3. Subscribe
### 3.1. Orders
subscribe new orders request
```JSON
{
    "Key": "hub_register",
    "DateTime": "2024-07-23T17:45:35.402254600Z",
    "ContentType": "json",
    "ContentText": "{\"RegistrationType\":1, \"Key\":\"order.created\"}"
}
```

subscribe new orders response
```JSON
{
    "Key": "hub_register_confirmed",
    "DateTime": "2024-08-02T15:07:35.069644200Z",
    "ContentType": "json",
    "ContentText": "{\"HubMessageId\":\"\",\"Response\":\"{\\\"Key\\\":\\\"order.updated\\\",\\\"RegistrationType\\\":0,\\\"Succeeded\\\":true,\\\"Message\\\":\\\"Registered key: order.created ClientToServer\\\"}\"}"
}
```

subscribe order updates request
```JSON
{
    "Key": "hub_register",
    "DateTime": "2024-07-23T17:45:35.402254600Z",
    "ContentType": "json",
    "ContentText": "{\"RegistrationType\":1, \"Key\":\"order.updated\"}"
}
```

subscribe order updates response
```JSON
{
    "Key": "hub_register_confirmed",
    "DateTime": "2024-08-02T15:07:35.069644200Z",
    "ContentType": "json",
    "ContentText": "{\"HubMessageId\":\"\",\"Response\":\"{\\\"Key\\\":\\\"order.updated\\\",\\\"RegistrationType\\\":0,\\\"Succeeded\\\":true,\\\"Message\\\":\\\"Registered key: order.updated ClientToServer\\\"}\"}"
}
```

## 4. Events
### 4.1. Orders
new order
```JSON
{
    "Key": "order.created",
    "DateTime": "2024-08-02T15:11:19.696423400Z",
    "ContentType": "json",
    "ContentText": "{\"SplitLeavesQty\":100.0,\"CumCashQty\":null,\"LeavesCashQty\":null,\"Financial\":3323.00,\"FinancialDone\":0.0,\"FinancialMarket\":0.0,\"DoneRate\":0.0,\"SplitRate\":0.0,\"PriceLiteral\":\"33.23\",\"SettlementTypeLiteral\":\"T+2\",\"OrdTypeLiteral\":\"Limit\",\"OrdStatusLiteral\":\"Requested\",\"TimeInfForceLiteral\":\"Day\",\"PutOrCallLiteral\":\"\",\"OneLetterSide\":\"B\",\"LocalTransactTime\":\"2024-08-02T12:11:19.313-03:00\",\"LocalCreated\":\"2024-08-02T12:11:19.6886671-03:00\",\"LocalUpdated\":\"2024-08-02T12:11:19.6886671-03:00\",\"NotificationStatusLiteral\":\"NO\",\"ToUpdateReport\":\"B BPAC11 0 0 0\",\"Indicator\":3,\"IsLeg\":false,\"IsCashQty\":false,\"Id\":\"439c5e18-b21e-43da-960a-a7874dc5e983\",\"ClientId\":\"CLIENT_UAT\",\"EntityId\":\"5f6a6041-4672-4eb3-89ac-0576f2b42ada\",\"ExecBroker\":\"935\",\"Account\":\"114\",\"ExchangeAccountId\":\"0607b3ee-974c-4f88-aa05-a23acd869582\",\"VSOTValue\":0.0,\"VSOTVolume\":0.0,\"VSOTShare\":0.0,\"FinancialConsideration\":0.0,\"FinancialGrossConsideration\":0.0,\"AvgPx\":0.0,\"ClOrdId\":\"48641f6d75e247ec9a7d08d6999b04bb\",\"Currency\":null,\"CumQty\":0.0,\"HandIInst\":\"1\",\"ExpireDate\":\"2024-08-02T00:00:00\",\"LastPx\":null,\"LastQty\":null,\"LeavesQty\":100.0,\"StopPx\":null,\"MaxFloor\":null,\"MinQty\":null,\"OrderId\":null,\"TradeDate\":\"2024-08-02T00:00:00\",\"Qty\":100.0,\"CashOrderQty\":null,\"OrdStatus\":\"X\",\"OrdType\":\"2\",\"OrigClOrdId\":null,\"Price\":33.23,\"Side\":1,\"Symbol\":\"BPAC11\",\"SecurityIdSource\":\"8\",\"SecurityId\":\"BPAC11\",\"TimeInForce\":\"0\",\"Text\":null,\"TransactTime\":\"2024-08-02T15:11:19.313Z\",\"SecondaryOrderId\":null,\"Country\":\"BR\",\"SourceSession\":null,\"RoutedSession\":\"Session \u003cOMS-UAT-SOLUTIONS-OE-TO-B3-C935A-UAT, FIX-GATEWAY-UAT, FIX.4.4\u003e\",\"Exchange\":\"BVMF\",\"Subaccount\":null,\"Rejected\":false,\"RejectReason\":null,\"BasketRefId\":null,\"BasketLabel\":null,\"Reportable\":false,\"ServiceId\":\"B3\",\"DeskId\":null,\"AssignedUser\":\"<EMAIL>\",\"BusinessUnit\":null,\"AssignedUserId\":\"b3a95e93-0d62-4af8-ba3f-e45c9b09a3d7\",\"EnteredByUser\":\"<EMAIL>\",\"SplittedQty\":0.0,\"Memo\":\"\",\"LastExecutionTime\":null,\"Channel\":3,\"NotificationStatus\":1,\"RelatedOrderId\":null,\"RelatedCompositeId\":null,\"IsComposite\":false,\"ExternalId\":\"439c5e18-b21e-43da-960a-a7874dc5e983\",\"WorkingIndicator\":null,\"IsAlive\":false,\"IsBasket\":false,\"IsCompleted\":false,\"Created\":\"2024-08-02T15:11:19.6886671Z\",\"Updated\":\"2024-08-02T15:11:19.6886671Z\",\"IsActive\":true,\"EnteringTrader\":\"<EMAIL>\",\"Asset\":null,\"ExecId\":null,\"ExecRefId\":null,\"CrossId\":null,\"IsManageable\":false,\"ClientJustification\":null,\"TimeJustification\":null,\"MarketFxRate\":null,\"DealtFxRate\":null,\"ForwardDays\":null,\"ForwardCashOrderId\":null,\"Source\":null,\"IsAggression\":true,\"SupportDescription\":\"
    \",\"SupportType\":null,\"IsUnsolicitedCancel\":false,\"HighlightUnsolicitedCancel\":false,\"ProtectionPrice\":null,\"BasketInputIndex\":null,\"SecurityType\":null,\"UnderlyingSymbol\":null,\"PutOrCall\":null,\"StrikePrice\":null,\"MaturityDt\":null,\"ContractMultiplier\":null,\"SettlementType\":\"3\",\"ApprovalStatus\":0,\"SettlCurrency\":null,\"Tag\":null,\"ForexReq\":false,\"IsEligibleToSplit\":false,\"MaxFloorStrategy\":0,\"EstimatedBps\":null,\"EstimatedFinancial\":null,\"ExerciseStyle\":null,\"OrderCapacity\":null,\"CoveredOrUncovered\":null,\"PositionEffect\":null,\"ComplianceStatus\":0,\"Strategy\":null,\"FixedRate\":null,\"DaysToSettlement\":null,\"TransactionType\":null,\"ReferencePrice\":33.17,\"LocateBroker\":null,\"LocationId\":null,\"LocateReqd\":null,\"RelatedMultilegOrderId\":null,\"RetailLiquidityTaker\":null,\"PegPriceType\":null,\"ExecRestatementReason\":0,\"ExternalBroker\":null,\"LastMkt\":null,\"HighlightForceMatchStrategies\":false,\"SelfTradePreventionInstruction\":null}"
}
```

order update
```JSON
{
    "Key": "order.updated",
    "DateTime": "2024-08-02T15:11:19.725185Z",
    "ContentType": "json",
    "ContentText": "{\"SplitLeavesQty\":100.0,\"CumCashQty\":null,\"LeavesCashQty\":null,\"Financial\":3323.00,\"FinancialDone\":0.0,\"FinancialMarket\":0.0,\"DoneRate\":0.0,\"SplitRate\":0.0,\"PriceLiteral\":\"33.23\",\"SettlementTypeLiteral\":\"T+2\",\"OrdTypeLiteral\":\"Limit\",\"OrdStatusLiteral\":\"New\",\"TimeInfForceLiteral\":\"Day\",\"PutOrCallLiteral\":\"\",\"OneLetterSide\":\"B\",\"LocalTransactTime\":\"2024-08-02T12:11:19.313-03:00\",\"LocalCreated\":\"2024-08-02T12:11:19.6886671-03:00\",\"LocalUpdated\":\"2024-08-02T12:11:19.7176685-03:00\",\"NotificationStatusLiteral\":\"NO\",\"ToUpdateReport\":\"B BPAC11 0 0 0\",\"Indicator\":3,\"IsLeg\":false,\"IsCashQty\":false,\"Id\":\"439c5e18-b21e-43da-960a-a7874dc5e983\",\"ClientId\":\"CLIENT_UAT\",\"EntityId\":\"5f6a6041-4672-4eb3-89ac-0576f2b42ada\",\"ExecBroker\":\"935\",\"Account\":\"114\",\"ExchangeAccountId\":\"0607b3ee-974c-4f88-aa05-a23acd869582\",\"VSOTValue\":0.0,\"VSOTVolume\":0.0,\"VSOTShare\":0.0,\"FinancialConsideration\":0.0,\"FinancialGrossConsideration\":0.0,\"AvgPx\":0.0,\"ClOrdId\":\"48641f6d75e247ec9a7d08d6999b04bb\",\"Currency\":null,\"CumQty\":0.0,\"HandIInst\":\"1\",\"ExpireDate\":\"2024-08-02T00:00:00\",\"LastPx\":null,\"LastQty\":null,\"LeavesQty\":100.0,\"StopPx\":null,\"MaxFloor\":null,\"MinQty\":null,\"OrderId\":\"82113561908\",\"TradeDate\":\"2024-08-02T00:00:00\",\"Qty\":100.0,\"CashOrderQty\":null,\"OrdStatus\":\"0\",\"OrdType\":\"2\",\"OrigClOrdId\":null,\"Price\":33.23,\"Side\":1,\"Symbol\":\"BPAC11\",\"SecurityIdSource\":\"8\",\"SecurityId\":\"BPAC11\",\"TimeInForce\":\"0\",\"Text\":null,\"TransactTime\":\"2024-08-02T15:11:19.313Z\",\"SecondaryOrderId\":\"8273254178\",\"Country\":\"BR\",\"SourceSession\":null,\"RoutedSession\":\"Session \u003cOMS-UAT-SOLUTIONS-OE-TO-B3-C935A-UAT, FIX-GATEWAY-UAT, FIX.4.4\u003e\",\"Exchange\":\"BVMF\",\"Subaccount\":null,\"Rejected\":false,\"RejectReason\":null,\"BasketRefId\":null,\"BasketLabel\":null,\"Reportable\":false,\"ServiceId\":\"B3\",\"DeskId\":null,\"AssignedUser\":\"<EMAIL>\",\"BusinessUnit\":null,\"AssignedUserId\":\"b3a95e93-0d62-4af8-ba3f-e45c9b09a3d7\",\"EnteredByUser\":\"<EMAIL>\",\"SplittedQty\":0.0,\"Memo\":\"\",\"LastExecutionTime\":null,\"Channel\":3,\"NotificationStatus\":1,\"RelatedOrderId\":null,\"RelatedCompositeId\":null,\"IsComposite\":false,\"ExternalId\":\"82113561908\",\"WorkingIndicator\":false,\"IsAlive\":true,\"IsBasket\":false,\"IsCompleted\":false,\"Created\":\"2024-08-02T15:11:19.6886671Z\",\"Updated\":\"2024-08-02T15:11:19.7176685Z\",\"IsActive\":true,\"EnteringTrader\":\"<EMAIL>\",\"Asset\":null,\"ExecId\":\"820616000000000457\",\"ExecRefId\":null,\"CrossId\":null,\"IsManageable\":false,\"ClientJustification\":null,\"TimeJustification\":null,\"MarketFxRate\":null,\"DealtFxRate\":null,\"ForwardDays\":null,\"ForwardCashOrderId\":null,\"Source\":null,\"IsAggression\":true,\"SupportDescription\":\"\",\"SupportType\":null,\"IsUnsolicitedCancel\":false,\"HighlightUnsolicitedCancel\":false,\"ProtectionPrice\":null,\"BasketInputIndex\":null,\"SecurityType\":null,\"UnderlyingSymbol\":null,\"PutOrCall\":null,\"StrikePrice\":null,\"MaturityDt\":null,\"ContractMultiplier\":null,\"SettlementType\":\"3\",\"ApprovalStatus\":0,\"SettlCurrency\":null,\"Tag\":null,\"ForexReq\":false,\"IsEligibleToSplit\":true,\"MaxFloorStrategy\":0,\"EstimatedBps\":null,\"EstimatedFinancial\":null,\"ExerciseStyle\":null,\"OrderCapacity\":null,\"CoveredOrUncovered\":null,\"PositionEffect\":null,\"ComplianceStatus\":0,\"Strategy\":null,\"FixedRate\":null,\"DaysToSettlement\":null,\"TransactionType\":null,\"ReferencePrice\":33.17,\"LocateBroker\":null,\"LocationId\":null,\"LocateReqd\":null,\"RelatedMultilegOrderId\":null,\"RetailLiquidityTaker\":null,\"PegPriceType\":null,\"ExecRestatementReason\":0,\"ExternalBroker\":null,\"LastMkt\":null,\"HighlightForceMatchStrategies\":false,\"SelfTradePreventionInstruction\":null}"
}
```