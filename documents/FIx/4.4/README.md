# API Fix 4.4

Fix API documentation to integrate with Solutions Order Entry.

- [Authentication](#authentication)
- [Obs](#obs)
- [Message Type](#message-type)
- [Message Specification](#message-specification)
  - [Standard Headers and Trailer](#standard-headers-and-trailer)
  - [Session Level Messages](#session-level-messages)
  - [Application Level Messages](#application-level-messages)
  - [Fields](#fields)

## Authentication

To authenticate you will need a tls certificate, SenderCompID, targetCompId, Username and Password all provide by our team.

## Obs

This document describes how to send and manage orders for Brazil markets, for Latam and US markets aditional instrument identification is needed.

## Message Type

| Message | MsgType | Flow |
| --- | --- | --- |
| [New Order](#new-order-single) | D | Send |
| [Replace](#replace) | G | Send |
| [Cancel](#cancel) | F | Send |
| [New Order Cross](#new-order-cross) | s | Send |
| [Business Reject](#business-reject) | j | Receive |
| [Reject](#reject) | 3 | Receive |
| [Execution Report](#execution-report) | 8 | Receive |
| [Cancel Reject](#cancel-reject) | 9 | Receive |

## Message Specification

### Standard Headers and Trailer

#### Standard Headers

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 8 | BeginString | Y | String | | Valid value: FIX.4.4 = FIX Protocol Version 4.4 |
| 9 | BodyLength | Y | Int | | Always unencrypted, must be second field in message |
| 35 | MsgType | Y | String | | Always unencrypted, must be third field in message |
| 49 | SenderCompID | Y | String | | Assigned value used to identify the client sending the message |
| 56 | TargetCompID | Y | String | | Assigned value used to identify firm that originated the message if the message was delivered by a third party |
| 34 | MsgSeqNum | Y | Int | | Message Sequence Number |
| 50 | SenderSubID | N | String | | Assigned value used to identify a specific message originator (desk, trader, etc.) |
| 57 | TargetSubID | N | String | | Assigned value used to identify specific individual or unit intended |
| 52 | SendingTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |

#### Standard Trailer

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 10 | Checksum | Y | Int | | Always unencrypted, always last field in message |

### Session Level Messages

#### Login

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: A |
| 553 | Username | Y | String | | Provided Username |
| 554 | Password | Y | String | | Provided Password |

#### Logout

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: 5 |
| 58 | Text | N | String | | |

### Application Level Messages

#### New Order Single

##### DMA

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: D |
| 1 | Account | Y | Int | | Customer account at the Exchange |
| 11 | ClOrdID | Y | String | 34 | Unique order identifier set by the client |
| 21 | [HandlInst](#handlinst) | Y | String | | Valid value: 1 = Automated execution |
| 109 | ClientID | Y | String | | Client identification (Provided by Solutions team) |
| 22 | SecurityIDSource | Y | String | | Valid values: 8 = Exchange Symbol |
| 48 | SecurityID | Y | String | | Security ID as defined by exchange (Same as Symbol) |
| 207 | SecurityExchange | Y | String | | Valid values: BVMF = B3 Exchange |
| 55 | Symbol | Y | String | | Security symbol on the exchange |
| 15 | Currency | Y | String | | Valid values: BRL |
| 38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 110 | MinQty | N | Long | | |
| 1138 | DisplayQty | N | Long | | |
| 40 | [OrderType](#ordertype) | Y | Char | 1 | See [Fields](#fields) |
| 44 | Price | C | Float | | Limit Price for the orders (Required if OrdType == Limit) |
| 99 | StopPx | N | Float | | |
| 54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| 59 | [TimeInForce](#timeinforce) | N | Char | 1 | See [Fields](#fields) |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |
| 421 | Country | Y | String| | Valid values: BR |
| 453 | NoPartyIDs | Y | Int | | Expected value >= 2, Repeating group |
| 1>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 1>448 | PartyID | Y | String | | Expected value: Broker Code |
| 1>452 | PartyRole | Y | Int | | Expected value: 7=ENTERING FIRM |
| 2>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 2>448 | PartyID | Y | String | | Expected value: Trader identification |
| 2>452 | PartyRole | Y | Int | | Expected value: 36=ENTERING TRADER |

##### CARE

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: D |
| 11 | ClOrdID | Y | String | 34 | Unique order identifier set by the client |
| 21 | [HandlInst](#handlinst) | Y | String | | Valid value: 3 = Manual execution |
| 109 | ClientID | Y | String | | Client identification (Provided by Solutions team) |
| 22 | SecurityIDSource | Y | String | | Valid values: 8 = Exchange Symbol |
| 48 | SecurityID | Y | String | | Security ID as defined by exchange (Same as Symbol) |
| 207 | SecurityExchange | Y | String | | Valid values: BVMF = B3 Exchange |
| 55 | Symbol | Y | String | | Security symbol on the exchange |
| 15 | Currency | Y | String | | Valid values: BRL |
| 38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 110 | MinQty | N | Long | | |
| 1138 | DisplayQty | N | Long | | |
| 40 | OrderType | Y | Char | 1 | See [Fields](#fields) |
| 44 | Price | C | Float | | Limit Price for the orders (Required if OrdType == Limit) |
| 99 | StopPx | N | Float | | |
| 54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| 59 | [TimeInForce](#timeinforce) | N | Char | 1 | See [Fields](#fields) |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |
| 421 | Country | Y | String| | Valid values: BR |
| 453 | NoPartyIDs | Y | Int | | Expected value >= 2, Repeating group |
| 1>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 1>448 | PartyID | Y | String | | Expected value: Broker Code |
| 1>452 | PartyRole | Y | Int | | Expected value: 7=ENTERING FIRM |
| 2>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 2>448 | PartyID | Y | String | | Expected value: Trader identification |
| 2>452 | PartyRole | Y | Int | | Expected value: 36=ENTERING TRADER |

#### Replace

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: G |
| 1 | Account | C | Int | | Customer account at the Exchange (If DMA) |
| 11 | ClOrdID | Y | String | 34 | Unique order identifier set by the client (Always new identifier) |
| 41 | OrigClOrdID | Y | String | 34 | Unique order identifier |
| 21 | [HandlInst](#handlinst) | Y | String | | Same as original |
| 109 | ClientID | Y | String | | Client identification (Provided by Solutions team) |
| 22 | SecurityIDSource | Y | String | | Valid values: 8 = Exchange Symbol |
| 48 | SecurityID | Y | String | | Security ID as defined by exchange (Same as Symbol) |
| 207 | SecurityExchange | Y | String | | Valid values: BVMF = B3 Exchange |
| 55 | Symbol | Y | String | | Security symbol on the exchange |
| 15 | Currency | Y | String | | Valid values: BRL |
| 38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 110 | MinQty | N | Long | | |
| 1138 | DisplayQty | N | Long | | |
| 40 | OrderType | Y | Char | 1 | See [Fields](#fields) |
| 44 | Price | C | Float | | Limit Price for the orders (Required if OrdType == Limit) |
| 99 | StopPx | N | Float | | |
| 54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| 59 | [TimeInForce](#timeinforce) | N | Char | 1 | See [Fields](#fields) |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |
| 421 | Country | Y | String| | Valid values: BR |
| 453 | NoPartyIDs | Y | Int | | Expected value >= 2, Repeating group |
| 1>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 1>448 | PartyID | Y | String | | Expected value: Broker Code |
| 1>452 | PartyRole | Y | Int | | Expected value: 7=ENTERING FIRM |
| 2>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 2>448 | PartyID | Y | String | | Expected value: Trader identification |
| 2>452 | PartyRole | Y | Int | | Expected value: 36=ENTERING TRADER |

#### Cancel

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: F |
| 11 | ClOrdID | Y | String | 34 | Unique order identifier set by the client (Always new identifier) |
| 37 | OrderID | Y | String | | Order identifier |
| 41 | OrigClOrdID | Y | String | 34 | Unique order identifier |
| 109 | ClientID | Y | String | | Client identification (Provided by Solutions team) |
| 38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |

#### New Order Cross

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: s |
| 21 | [HandlInst](#handlinst) | Y | String | | Valid value: 1 = Automated execution |
| 548 | CrossID | Y | String | 38 | Must be unique during a given trading day |
| 549 | CrossType | Y | Int | | Expected value: 1 Cross Trade Which Is Executed Completely Or Not |
| 550 | CrossPrioritization | Y | Int | | Expected value: 0 = None |
| 552 | NoSides | Y | Int | | Expected value: 2 |
| >54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| >11 | ClOrdID | Y | String | 34 | Unique order identifier set by the client |
| >453 | NoPartyIDs | Y | Int | | Expected value >= 2, Repeating group |
| >>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| >>448 | PartyID | Y | String | | Respective value from the specified PartyRole |
| >>452 | [PartyRole](#partyrole) | Y | Int | | See [Fields](#fields) |
| >1 | Account | Y | Int | | Customer account at the Exchange |
| >38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 55 | Symbol | Y | String | | Security symbol on the exchange |
| 22 | SecurityIDSource | Y | String | | Valid values: 8 = Exchange Symbol |
| 48 | SecurityID | Y | String | | Security ID as defined by exchange (Same as Symbol) |
| 207 | SecurityExchange | Y | String | | Valid values: BVMF = B3 Exchange |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |
| 40 | [OrderType](#ordertype) | Y | Char | 1 | See [Fields](#fields) |
| 44 | Price | Y | Float | | Limit Price for the orders |
| 5149 | Memo | N | String | 50 | Free text to be used by the client |
| 2523 | [CrossedIndicator](#crossedindicator) | N | Int | | See [Fields](#fields) |

#### Execution Report

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: 8 |
| 150 | [ExecType](#exectype) | Y | Char | | See [Fields](#fields) |
| 11 | ClOrdID | Y | String | 34 | Active order identifier |
| 41 | OrigClOrdID | Y | String | 34 | If present on request |
| 17 | ExecID | Y | String | | |
| 37 | OrderID | Y | String | | Order identifier |
| 39 | [OrdStatus](#ordstatus) | Y | String | | See [Fields](#fields) |
| 1 | Account | C | Int | | Customer account at the Exchange (If DMA) |
| 6 | AvgPx | C | Float | | |
| 14 | CumQty | C | Long | | |
| 15 | Currency | Y | String | | Valid values: BRL |
| 38 | OrderQty | Y | Long | | Number of shares or contracts ordered |
| 44 | Price | C | Float | | If present on request |
| 54 | [Side](#side) | Y | Char | 1 | See [Fields](#fields) |
| 55 | Symbol | Y | String | | Security symbol on the exchange |
| 59 | [TimeInForce](#timeinforce) | N | Char | 1 | See [Fields](#fields) |
| 110 | MinQty | C | Long | | If present on request |
| 151 | LeavesQty | C | Long | | |
| 1138 | DisplayQty | C | Long | | If present on request |
| 60 | TransactTime | Y | UTC | | Expected format YYYYMMDD-HH:mm:ss.fff |
| 6032 | UniqueTradeId | N | String | | |

#### Reject

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: 3 |
| 11 | ClOrdID | Y | String | 34 | Active order identifier |
| 58 | Text | N | String | | |
| 372 | RefMsgType | Y | String | | MsgType of the message that got the rejection |
| 371 | RefTagID | Y | Int | | |
| 373 | SessionRejectReason | Y | String | | |

#### Business Reject

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: j |
| 11 | ClOrdID | Y | String | 34 | Active order identifier |
| 58 | Text | N | String | | |
| 372 | RefMsgType | Y | String | | MsgType of the message that got the rejection |
| 380 | BusinessRejectReason | Y | String | | Reason that lead to this rejection |

#### Cancel Reject

| Tag | Tag Name | Req | Type | Max Size | Comment |
| --- | --- | --- | --- | --- | --- |
| 35 | MsgType | Y | String | | Expected value: 9 |
| 150 | [ExecType](#exectype) | Y | Char | | Expected value: 8 (REJECTED) [Fields](#fields) |
| 39 | [OrdStatus](#ordstatus) | Y | String | | Expected value: 8 (REJECTED) See [Fields](#fields) |
| 37 | OrderID | Y | String | | Order identifier |
| 11 | ClOrdID | Y | String | 34 | Active order identifier |
| 41 | OrigClOrdID | Y | String | 34 | If present on request |
| 434 | CxlRejResponseTo | N | String | | |
| 58 | Text | N | String | | |
| 1 | Account | C | Int | | Customer account at the Exchange (If DMA BR) |
| 55 | Symbol | Y | String | | |
| 453 | NoPartyIDs | Y | Int | | Expected value 1, Repeating group |
| 1>447 | PartyIDSource | Y | Char | | Expected value: D=PROPRIETARY CUSTOM CODE |
| 1>448 | PartyID | Y | String | | Expected value: ExternalId |
| 1>452 | PartyRole | Y | Int | | Expected value: 33=StrategyID |
| 115 | OnBehalfOfCompID | C | String | | if present on request |
| 116 | OnBehalfOfSubID | C | String | | if present on request |

### Fields

#### Side

Type: Char
| Value | Name |
| --- | --- |
| 1 | Buy |
| 2 | Sell |
| 5 | Short Sell |

#### OrderType

Type: Char

Obs: Market (Brazil and Chile only), Stop Limit (Brazil only), Market on Close (Brazil and Mexico only)

| Value | Name |
| --- | --- |
| 1 | Market |
| 2 | Limit |
| 4 | Stop Limit |

#### HandlInst

Type: Char

| Value | Name |
| --- | --- |
| 1 | Automated execution |
| 3 | Manual |

#### TimeInForce

Type: Char

| Value | Name |
| --- | --- |
| 0 | Day |
| 1 | Good Till Cancel (GTC) |
| 2 | At the Opening (MOP) |
| 3 | Immediate or Cancel (IOC) |
| 4 | Fill or Kill (FOK) |
| 6 | Good Till Date (GTD) |
| 7 | At the Close (MOC / LOC) |
| A | Good for Auction (MOA) |

#### ExecType

Type: Char

| Value | Name |
| --- | --- |
| 0 | New |
| 3 | Done for day |
| 4 | Canceled |
| 5 | Replaced |
| 6 | Pending Cancel |
| 8 | Rejected |
| C | Expired |
| D | Restated |
| F | Trade |

#### OrdStatus

Type: Char

| Value | Name |
| --- | --- |
| 0 | New |
| 1 | Partially filled |
| 2 | Filled |
| 3 | Done for day |
| 4 | Canceled |
| 5 | Replaced |
| 6 | Pending Cancel |
| 8 | Rejected |
| C | Expired |

#### PartyRole

Type: Int

| Value | Name |
| --- | --- |
| 5 | Investor Id |
| 7 | Entering Firm |
| 36 | Entering Trader |
| 54 | Sender Location |
| 76 | Desk ID |

#### CrossedIndicator

Type: Int

| Value | Name |
| --- | --- |
| 1001 | Structured transaction |
| 1002 | Operational error |
| 1003 | TWAP/VWAP |
