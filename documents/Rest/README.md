# API Rest
 
Rest API documentation to integrate with Solutions Order Entry.
 
## Summary
- [Authentication](#authentication)
- [Observation](#observation)
- [Create Order](#create-order)
  - [Required parameters](#required-parameters)
  - [Create Order (DMA/Limit)](#create-order-dmalimit)
  - [Create Order (DMA/Market)](#create-order-dmamarket)
  - [Create Order (DMA/StopLimit)](#create-order-dmastoplimit)
- [Change order](#change-order)
  - [Change Order (DMA/Limit)](#change-order-dmalimit)
  - [Change Order (DMA/Market)](#change-order-dmamarket)
  - [Change Order (DMA/StopLimit)](#change-order-dmastoplimit)
- [Query and read](#query-and-read)
  - [Query Order By Id](#query-order-by-id)
  - [Consult All Orders](#consult-all-orders)
- [Cancel](#cancel)
  - [Cancel Order By Id](#cancel-order-by-id)
  - [Cancel All User Order](#cancel-all-user-order)
- [Algos](#algos)
  - [TWAP](#twap)
  - [VWAP](#vwap)
  - [POV](#pov)
  - [PEGGED](#pegged)
  - [SNIPER](#sniper)
  - [PEGGED-SNIPER](#pegged-sniper)
  - [ICEBERG](#iceberg)
  - [TARGETCLOSE](#targetclose)
- [Dictionary](#dictionary)
 
## Authentication
 
It is necessary to provide a Bearer authorization token in the header of all
calls and requests to the REST API. In this documentation, you may notice that a token
is required when seeing the following character set: `{token}`. Consider replacing
this field with your own authorization token.
 
## Observation
 
This document describes how to submit and manage orders for the onshore market
(Brazil), for offshore integration additional fields are required for instrument identification.
 
Depending on the library or tool that you are using to request our API, it may be necessary to set headers
| Header | Value|
| ---|---|
| **User-Agent** | {generic value} |
| **Content-Type** | application/json |
 
 
---
## Create Order
 
#### Required parameters
|Tag|Key|Required|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|55|symbol|Y|String|-|N|Ticker symbol. Common, "human understood" representation of the security|
|54|[side](#side)|Y|String|-|N|Side of order. Valid values "B" or "S". Check for details at [Side](#side)|
|53|qty|Y|Decimal|-|Y|Overall/total quantity|
|1|account|Y|Integer|-|N|Specifies the account.|
|76|execBroker|Y|String|-|N|Specifies the broker code.|
|40|[ordType](#ordtype)|Y|String|-|Y|Specifies the order type.|
|59|[timeInForce](#timeinforce)|Y|String|-|Y|Specifies how long the order remains in effect. |
|44|price|Y|Decimal|-|Y|Price per unit of quantity|
|21|[isDMA](#isdma)|Y|Boolean|-|N|Indicates whether an order is DMA. If not, the order is classified as CARE.|
|109|[entity](#entity)|Y|String|-|N|Specifies the entity.|
|5149|memo|N|String|-|N|Custom field.|
 
 
 
#### Create Order (DMA/Limit)
 
#### POST /v1/order
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "symbol": "PETR4",                
 "side": "S",                      
 "qty": 200,                        
 "account": 114,                    
 "execBroker": "935",              
 "ordType": "Limit",                
 "timeInForce": "day",              
 "price": 90.56,                    
 "isDMA": true,                    
 "entity": "CLIENT_UAT",            
 "memo": "TEXT"                    
}
```
 
### Response
#### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
Attention, the code that returns as the order creation response, example:
`deaa13ad1cd74868a591b70fe8e93523`, will be used as the id to send a [Change Order (DMA/Limit)](#change-order-dmalimit)
or [Cancel Order By Id](#cancel-order-by-id)
 
### Create Order (DMA/MARKET)
#### POST /v1/order
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "symbol": "PETR4",                
 "side": "S",                      
 "qty": "200",                      
 "account": "114",                  
 "execBroker": "935",              
 "ordType": "Market",              
 "timeInForce": "day",              
 "price": "90.56",                  
 "isDMA": "true",                  
 "entity": "CLIENT_UAT",            
 "memo": "TEXT"                    
}
```
#### Response
##### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
Attention, the code returned as the order creation response, example:
`deaa13ad1cd74868a591b70fe8e93523`, will be used as the id to send a [Change Order (DMA/Market)](#change-order-dmamarket)
or [Cancel Order By Id](#cancel-order-by-id)
 
### Create Order (DMA/StopLimit)
### POST
#### /v1/order
 
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "symbol": "PETR4",                
 "side": "S",                      
 "qty": "200",                      
 "account": "114",                  
 "execBroker": "935",              
 "ordType": "Stop limit",          
 "timeInForce": "day",              
 "price": "90.56",                  
 "isDMA": "true",                  
 "entity": "CLIENT_UAT",            
 "memo": "TEXT"                    
}
```
#### Response
##### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
Attention, the code returned as the order creation response, example:
“deaa13ad1cd74868a591b70fe8e93523”, will be used as the id to send a [Change Order (DMA/Limit)](#change-order-dmalimit)
or [Cancel Order By Id](#cancel-order-by-id)
 
---
 
## Change order
 
#### Required parameters
|Tag|Key|Required|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|-|id|Y|String|-|N|ID returned as result from [Create Order](#create-order) request|
|55|symbol|Y|String|-|N|Ticker symbol. Common, "human understood" representation of the security|
|54|[side](#side)|Y|String|-|N|Side of order. Valid values "B" or "S"|
|53|qty|Y|Decimal|-|Y|Overall/total quantity|
|1|[account](#account)|Y|Integer|-|N|Specifies the account.|
|76|execBroker|Y|String|-|N|Specifies the broker code.|
|40|[ordType](#ordtype)|Y|String|-|Y|Specifies the order type.|
|59|[timeInForce](#timeinforce)|Y|String|-|Y|Specifies how long the order remains in effect. |
|44|price|Y|Decimal|-|Y|Price per unit of quantity, remove this tag to replace order to Market|
|109|[entity](#entity)|Y|String|-|N|Specifies the entity.|
 
### Change Order (DMA/Limit)
#### PUT /v1/order/
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "id": "deaa13ad1cd74868a591b70fe8e93523",      
 "ordType": "Limit",                            
 "account": "114",                              
 "execBroker": "935",                          
 "timeInForce": "Day",                          
 "price": 10,                                  
 "qty": 200                                    
}
```
 
#### Response
#### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
 
### Change Order (DMA/Market)
#### PUT /v1/order
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "id": "deaa13ad1cd74868a591b70fe8e93523",      
 "ordType": "Market",                          
 "account": "114",                              
 "execBroker": "935",                          
 "timeInForce": "Day",                          
 "qty": 200                                    
}
 
```
#### Response
#### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
 
## Change Order (DMA/StopLimit)
### PUT /v1/order
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
```JSON
{
 "id": "deaa13ad1cd74868a591b70fe8e93523",      
 "ordType": "Stop limit",                      
 "account": "114",                              
 "execBroker": "935",                          
 "timeInForce": "Day",                          
 "qty": 200                                    
}
 
```
### Response
#### 202 Accept
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
## Query and Read
 
### Parameters result
|Tag|Name|Type|Comment|
|---|---|---|---|
|17|execId|String|Specifies the unique identifier of the execution message as assigned by the sell-side.|
|37|orderID|String|Specifies the order identifier as assigned by sell-side.|
|11|clOrdId|String|Specifies the order identifier as assigned by the institution.|
|55|symbol|String|Instrument identification.|
|44|price|Decimal|Order price|
|99|stopPx|Decimal|Specifies the stop price.|
|110|minQty|Decimal|Specifies the quantity to be displayed in the order.|
|111|maxFloor|Decimal|Specifies the apparent lot.|
|636|workingIndicator|Boolean|Indicates whether the order is still being worked on.|
|41|origClOrdID|String|Specifies the order origin identifier as assigned by the institution.|
|198|secondaryOrderID|String|Specifies the order's secondary identifier as assigned by sell-side.|
|58|text|String|Custom field. Display rejection reason when [ExecType](#execType) is Rejected|
||memo|String|Custom field. Returns the value sent on [Create Order](#create-order)|
|1|account|Integer|Specifies the account.|
||resolvedAssignedUser|String|Identifies the user.|
|109|[entity](#entity)|String|Specifies the entity.|
||execBroker|Integer|Specifies the broker code.|
|1180|applId|String|Identifies the application with which a message is associated|
|75|tradeDate|Date|Specifies the trade creation date.|
|54|side|String|Identifies the side of the order.|
|53|qty|Decimal|Specifies the order quantity.|
|150|[execType](#exectype)|String|Specifies the type of execution. |
|6|avgPx|Decimal|Specifies the average price.|
|14|cumQty|Decimal|Specifies the cumulative quantity executed.|
|40|[ordType](#ordtype)|String|Specifies the order type. |
|60|transactTime|[DateTime](#datetime)|Specifies the execution time.|
|59|[timeInForce](#timeinforce)|[Date](#date)|Specifies how long the order remains in effect. |
|432|expireDate|[DateTime](#datetime)|Specifies the trade expiration date. Applicable to orders with TimeInForce equal to Good Till Date (GTD) |
|39|[ordStatus](#ordstatus)|String|Specifies the current OrdStatus. |
||service|String|Specifies the service used.|
 
### Query Order By Id
### GET /v1/order/id/{id}
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
Not applicable.
 
### Response
#### 200 Ok
``` JSON
[
 {
 "execId": "840511000000001621",
 "orderID": "8493469644",
 "clOrdId": "4897c8f64fb64d9c8b41b08d0496ad07",
 "symbol": "ALPA4",
 "price": 8.96000000,
 "stopPx": null,
 "minQty": null,
 "maxFloor": null,
 "workingIndicator": "N",
 "origClOrdID": null,
 "secondaryOrderID": "8443791619",
 "text": null,
 "memo": null,
 "account": "114",
 "resolvedAssignedUser": "user@teste.com",
 "entity": "CLIENT_UAT",
 "execBroker": "935",
 "applId": "AABB054",
 "tradeDate": "2023-07-19",
 "side": "Sell",
 "qty": "100",
 "execType": "New",
 "avgPx": "0.0000",
 "cumQty": "0",
 "ordType": "Limit",
 "transactTime": "2023-07-19T11:37:07.656-03:00",
 "timeInForce": "Day",
 "expireDate": "2023-07-19T03:00",
 "ordStatus": "New",
 "service": "DMA"
 }
]
 
```
 
### Consult All Orders
#### GET /v1/order/
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
Not applicable.
 
### Response
#### 200 Ok
``` JSON
[
 {
 "execId": "840511000000001621",
 "orderID": "8493469644",
 "clOrdId": "4897c8f64fb64d9c8b41b08d0496ad07",
 "symbol": "ALPA4",
 "price": 8.96000000,
 "stopPx": null,
 "minQty": null,
 "maxFloor": null,
 "workingIndicator": "N",
 "origClOrdID": null,
 "secondaryOrderID": "8443791619",
 "text": null,
 "memo": null,
 "account": "114",
 "resolvedAssignedUser": "user@teste.com",
 "entity": "CLIENT_UAT",
 "execBroker": "935",
 "applId": "AABB054",
 "tradeDate": "2023-07-19",
 "side": "Sell",
 "qty": "100",
 "execType": "New",
 "avgPx": "0.0000",
 "cumQty": "0",
 "ordType": "Limit",
 "transactTime": "2023-07-19T11:37:07.656-03:00",
 "timeInForce": "Day",
 "expireDate": "2023-07-19T03:00",
 "ordStatus": "New",
 "service": "DMA"
 },
 {
 "execId": "740504000000008401",
 "orderID": null,
 "clOrdId": "f9d23e3a802d4cfab328cd64f1d2d49a",
 "symbol": "PETR4",
 "price": 4807.00000000,
 "stopPx": null,
 "minQty": null,
 "maxFloor": null,
 "workingIndicator": null,
 "origClOrdID": null,
 "secondaryOrderID": null,
 "text": "[B3]The user is not authorized to trade",
 "memo": null,
 "account": "114",
 "resolvedAssignedUser": "user@teste.com",
 "entity": "CLIENT_UAT",
 "execBroker": "935",
 "applId": "AABB054",
 "tradeDate": "2023-07-19",
 "side": "Sell",
 "qty": "100",
 "execType": "Rejected",
 "avgPx": "0.0000",
 "cumQty": "0",
 "ordType": "Limit",
 "transactTime": "2023-07-19T11:45:37.426-03:00",
 "timeInForce": "Day",
 "expireDate": "2023-07-19T03:00",
 "ordStatus": "Rejected",
 "service": "DMA"
 },
]
 
```
 
## Cancel Order By Id
### DELETE /v1/order/`{id}`
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
Not applicable.
 
### Response
#### 202 Accepted
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
 
 
## Cancel All User Order
### DELETE /v1/order/myorders
#### Header
| Key | Value |
| --- | --- |
| Authorization | Bearer {token} |
 
#### Body
Not applicable.
 
### Response
#### 202 Accepted
``` JSON
{
 "message": "deaa13ad1cd74868a591b70fe8e93523"
}
```
---
 
## Algos
Below are the standard algos (for premium algos please consult Solutions team).
 
### Parameter default
Add to the body the fields `"strategy"` and `"strategyParameter"` like the example bellow.
 
#### Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "twap",
  "strategyParameter" : {
    // attributes
  }
}
```
 
For the `"strategy"` parameter, check out the list of [Strategies](#chooseStrategyName).
For the `"strategyParameter"` check out the parameters respectively
 
 
### Strategies
| strategyName        |
| ---                 |
| [twap](#twap)       |  
| [vwap](#vwap)       |  
| [pov](#pov)         |  
| [pegged](#pegged)   |
| [sniper](#sniper)   |
| [pegged/sniper](#pegged-sniper) |
| [iceberg](#iceberg) |
| [targetclose](#targetclose) |
 
 
 
### Strategy parameters
 
#### TWAP
 
|Tag|Key|Required|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|110|min-qty|N|Long|0|Y|Minimum quantity of the order to be executed|
|849|market-share|N|Decimal||Y|Maximum participation of the market volume (in percentage) |
|10044|would-price|N|Decimal||Y|Trigger price for the strategy|
|10014|consider-cross-orders|N|Bool|N|Y|Flag to indicate if cross orders should be considered in the execution|
|10015|consider-limit-price|N|Bool|N|Y|Flag to indicate if market executions above the limit price should be considered by the strategy|
|168|start-time|N|UTC||Y|Execution start time|
|126|end-time|N|UTC||Y|Execution end time|
|50007|would-qty|N|Long||Y|Number of shares or contracts for I Would|
|10020|would-max-floor|N|Long||Y|Maximum number of shares or contracts displayed by the I Would|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50004|auction-intraday|NN|Bool|N|Y|Order wants to participate in Intraday Auction with slices calculated for that period|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|

#### TWAP Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "twap",
  "strategyParameter" : {
    "max-floor": "100",    
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
 ```

#### VWAP
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|110|min-qty|N|Long|0|Y|Minimum quantity of the order to be executed|
|849|market-share|N|Decimal||Y|Maximum participation of the market volume (in percentage) |
|10044|would-price|N|Decimal||Y|Trigger price for the strategy|
|10014|consider-cross-orders|N|Bool|N|Y|Flag to indicate if cross orders should be considered in the execution|
|10015|consider-limit-price|N|Bool|N|Y|Flag to indicate if market executions above the limit price should be considered by the strategy|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|126|end-time|N|[UTC](#utc)||Y|Execution end time|
|50007|would-qty|N|Long||Y|Number of shares or contracts for I Would|
|10020|would-max-floor|N|Long||Y|Maximum number of shares or contracts displayed by the I Would|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50004|auction-intraday|NN|Bool|N|Y|Order wants to participate in Intraday Auction with slices calculated for that period|
|50006|tilt|N|Int|0|Y|VWAP type: 0 (Regular), 1 (Backloaded), 2 (Frontloaded)|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

#### VWAP Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "vwap",
  "strategyParameter" : {
    "max-floor": "100",    
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```

#### POV
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|99925|target-participation|Y|Decimal||Y|Target participation of the market volume (in percentage)|
|10044|would-price|N|Decimal||Y|Trigger price for the strategy|
|10014|consider-cross-orders|N|Bool|N|Y|Flag to indicate if cross orders should be considered in the execution|
|10015|consider-limit-price|N|Bool|N|Y|Flag to indicate if market executions above the limit price should be considered by the strategy|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|126|end-time|N|[UTC](#utc)||Y|Execution end time|
|50007|would-qty|N|Long||Y|Number of shares or contracts for I Would|
|10020|would-max-floor|N|Long||Y|Maximum number of shares or contracts displayed by the I Would|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50004|auction-intraday|NN|Bool|N|Y|Order wants to participate in Intraday Auction with slices calculated for that period|
|50008|participation-tolerance|N|Decimal|0|Y|Participation tolerance for target participation|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|
 
 #### POV Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "pov",
  "strategyParameter" : {
    "max-floor": "100",
    "target-participation": 10,    
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```


#### PEGGED
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|126|end-time|N|[UTC](#utc)||Y|Execution end time|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50010|offset|N|Int|0|Y|Ticks offset of best price|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

 #### POV Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "pegged",
  "strategyParameter" : {
    "max-floor": "100",
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```

 
#### SNIPER
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|110|min-qty|N|Long||Y|Minimum quantity of an order to be executed|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|126|end-time|N|[UTC](#utc)||Y|Execution end time|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

 #### SNIPER Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "sniper",
  "strategyParameter" : {    
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```


 
#### PEGGED/SNIPER
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|110|min-qty|N|Long||Y|Minimum quantity of an order to be executed|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|126|end-time|N|[UTC](#utc)||Y|Execution end time|
|50002|auction-leftover|N|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|N|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50010|offset|N|Int|0|Y|Ticks offset of best price|
|50011|sniper-price|Y|Decimal||Y|Trigger price for the strategy|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

 #### PEGGED/SNIPER Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "pegged/sniper",
  "strategyParameter" : {    
    "sniper-price": 90.56,
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```
 
#### ICEBERG
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|168|start-time|NN|[UTC](#utc)||Y|Execution start time|
|126|end-time|NN|[UTC](#utc)||Y|Execution end time|
|50002|auction-leftover|NN|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|NN|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50004|auction-intraday|NN|Bool|N|Y|Order wants to participate in Intraday Auction respecting its iceberg price|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage) |
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

 #### ICEBERG Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "iceberg",
  "strategyParameter" : {    
    "start-time": "20240515-20:00:00",
    "end-time": "20240515-21:00:00"
  }
}
```
 
#### TARGETCLOSE
|Tag|Key|Req|Type|Default|Amend|Comment|
|---|---|---|---|---|---|---
|111|max-floor|N|Long|Infinity|Y|Maximum displayed quantity|
|849|market-share|C|Decimal||Y|Maximum participation of the market volume. Required if no start time is set|
|168|start-time|N|[UTC](#utc)||Y|Execution start time|
|50002|auction-leftover|N|Bool|N|Y|Order wants to participate in End of day Auction with leftover quantity|
|50003|closing-auction-qty|N|Long|0|Y|Quantity to only be traded in the End of day Auction |
|50004|auction-intraday|N|Bool|N|Y|Order wants to participate in Intraday Auction with slices calculated for that period|
|50014|historical-on-close|N|Bool|N|Y|If a percentage of the order relative to the historical close volume should be reserved and only traded at the close|
|50020|close-auction-participation|N|Decimal||Y|Close auction target participation of the market volume (in percentage)|
|50021|max-close-auction-participation|N|Decimal||Y|Close auction maximum participation of the market volume (in percentage)|

 #### TARGETCLOSE Body example
``` json
{
  "symbol": "PETR4",
  "side": "S",
  "qty": "200",
  "account": "114",
  "execBroker": "935",
  "ordType": "Limit",
  "timeInForce": "day",
  "price": "90.56",
  "isDMA": "true",                    
  "entity": "CLIENT_UAT",            
  "memo": "TEXT",                    
  "strategy" : "targetclose",
  "strategyParameter" : {    
    "start-time": "20240515-20:00:00",
    "max-floor": "100"
  }
}
```
 
 
## Type Dictionary
 
### Side
Identifies the side of the order.
 
| Value | Description |
| --- | --- |
| B | Buy |
| S | Sell |
 
### OrdType
Specifies the order type.
 
| Value | Description |
| --- | --- |
| Market | Market order |
| Limit | Limit order |
| Stop Limit | Ordem stop-limit |
 
### TimeInForce
Specifies how long the order remains in effect.
 
| Value | Description |
| --- | --- |
| "Day" | The order will operate throughout the trading hours of the day. (does not participate in the auction). |
| "Good Till Cancel” or "GTC" | The order will remain active until it is executed or canceled. |
| "At the Opening" or "MOP" | Order valid for the opening auction. |
| "Immediate or Cancel" or "IOC" | The order will be executed with the available quantity and immediately cancel the rest. |
| "Fill or Kill" or "FOK" | If the order is not executed in full, the entire order will be cancelled. |
| "Good Till Date" or "GTD" | This order will be valid until the date specified in the calendar below. |
| "At the Close" or "MOC" | Order to be sent in the Closing Call. |
| "Good for Auction" or "MOA" | Used for the opening auction and intraday auctions. |
 
 
### IsDMA
Indicates whether an order is DMA. If not, the order is classified as CARE.
 
| Value | Description |
| --- | --- |
| true | Order is classified as DMA |
| false | Order is classified as CARE |
 
### Entity
Specifies the entity.
Type: String.
**[Provided by the Solutions team.]**
 
### WorkingIndicator
Indicates whether the order is still being worked on.
 
| Value | String |
| --- | --- |
| true | Y |
| false | N |
 
### OrdStatus
Specifies the status of the order.
 
| Value | Description |
| --- | --- |
| New | New |
| Partial | Partially executed |
| Filled | Executed |
| Done for day | Order completed. It may have been partially executed or not executed at all. |
| Canceled | Canceled |
| Replaced | Amended |
| Pending Cancel | Pending cancellation |
| Rejected | Rejected |
| Pending New | Pending creation. |
| Expired | Expired |
| Pending Replace | Pending change |
 
### ExecType
Specifies the type of execution.
 
| Value | Description |
| --- | --- |
| New | New |
| Done for day | Order completed. It may have been partially executed or not executed at all. |
| Canceled | Canceled |
| Replaced | Amended |
| Pending Cancel | Pending cancellation |
| Rejected | Rejected |
| Pending New | Pending creation. |
| Expired | Expired |
| Restated | Modified. Change not requested by sellside. |
| Pending Replace | Pending cancellation |
| Trade | Executed or partially executed |
| Requested | Requested |
 
 
### Dictionary
 
#### UTC
yyyyMMdd-HH:mm:ss (UTC)
 
#### Date
yyyy-MM-dd or yyyy-MM-ddTHH:mm
 
#### DateTime (json format)
2024-04-25T11:03:25.603-03:00
yyyy-MM-ddTHH:mm:ss.mmmm-GMTHH:GMTMM
 