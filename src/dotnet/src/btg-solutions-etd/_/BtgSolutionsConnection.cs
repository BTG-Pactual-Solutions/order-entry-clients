namespace BtgPactualSolutions.Etd {
    using BtgPactualSolutions.Etd.core;
    using BtgPactualSolutions.Etd.core.messages;
    using BtgPactualSolutions.Etd.core.model;
    using BtgPactualSolutions.Etd.core.settings;
    using Microsoft.Extensions.Logging;
    using QuickFix;
    using QuickFix.Fields;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Reactive.Linq;
    using System.Reactive.Subjects;
    using System.Threading;
    using System.Threading.Tasks;

    public class BtgSolutionsConnection : IBtgSolutionsConnection {
        public bool IsConnected { get => _isConnect; }

        public IObservable<Message> ObservableMessage { get; private set; }
        public IObservable<ObservableExecutionReport> ObservableExecutionReport { get; private set; }
        public IObservable<ObservableOrderCancelReject> ObservableOrderCancelReject { get; private set; }
        public IObservable<ObservableBusinessMessageReject> ObservableBusinessMessageReject { get; private set; }
        public IObservable<ObservableReject> ObservableReject { get; private set; }
        public IObservable<Tuple<SessionID, bool>> ObservableSessionIsAlive { get; private set; }

        private readonly AutoResetEvent _autoResetEvent = new AutoResetEvent(false);
        private readonly IBtgSolutionsFixConnection _app;
        private readonly string _defaultClientId;
        private readonly ILogger _logger;
        private readonly InitiatorSettings _settings;

        private Session _defaultSession;
        private bool _isConnect;

        private ISubject<Message> _subjectMessage;
        private ISubject<ObservableExecutionReport> _subjectExecutionReport;
        private ISubject<ObservableOrderCancelReject> _subjectOrderCancelReject;
        private ISubject<ObservableBusinessMessageReject> _subjectBusinessMessageReject;
        private ISubject<ObservableReject> _subjectObservableReject;
        private ISubject<Tuple<SessionID, bool>> _subjectSessionIsActive;

        protected BtgSolutionsConnection() {
        }

        public BtgSolutionsConnection(ILogger logger, string senderCompID, string targetCompID, string socketConnectHost, long socketConnectPort,
                                       string SSLCertificate, string username, string password, string defaultClientId = null, FixSettings settings = null) {
            _logger = logger;
            _defaultClientId = defaultClientId;
            if (_logger == null) {
                throw new ArgumentNullException(nameof(logger));
            } else if (string.IsNullOrEmpty(senderCompID)) {
                throw new ArgumentNullException(nameof(senderCompID));
            } else if (string.IsNullOrEmpty(targetCompID)) {
                throw new ArgumentNullException(nameof(targetCompID));
            } else if (string.IsNullOrEmpty(socketConnectHost)) {
                throw new ArgumentNullException(nameof(socketConnectHost));
            } else if (socketConnectPort <= 0) {
                throw new ArgumentNullException(nameof(socketConnectPort));
            }

            _app = new BtgSolutionsFixConnection(logger, senderCompID, targetCompID, socketConnectHost, socketConnectPort, SSLCertificate, username, password, settings);
            _settings = _app.GetInitiatorSettings();

            SetObservable();
        }


        public void Start() {
            _app.Start();
            SetSessionId();
            _logger.LogInformation("Success Start");
        }

        public void Stop() {
            _app.Stop();
            _logger.LogInformation("Success Stop");
        }

        /// <summary>
        /// Send custom fix message
        /// throw NullReferenceException
        /// </summary>
        /// <param name="message"></param>
        public void SendFixMessage(Message message) {
            SendMessage(message);
        }

        public void NewOrderSingle(CreateNewOrderSingleRequest req) {
            _logger.LogInformation("Creating message new order single");
            try {
                if (req.ClientId == null && !string.IsNullOrEmpty(_defaultClientId)) {
                    req.ClientId = _defaultClientId;
                }
                EvaluateResult evaluateResult = req.Evaluate();
                if (evaluateResult.HasIssue()) {
                    throw new ArgumentNullException(nameof(req), evaluateResult.GetIssue());
                }
                Message message = BuildMessage(MsgType.NEWORDERSINGLE);

                // SenderSubID
                message.SetField(new StringField(Tags.SenderSubID, req.Trader));

                // ClientID
                message.SetField(new StringField(Tags.ClientID, req.ClientId));

                // Symbol
                message.SetField(new StringField(Tags.Symbol, req.Symbol));

                // Security settings
                SetIfHasValue(message, Tags.SecurityExchange, req.ExDestination, defaultValue: "BVMF");
                SetIfHasValue(message, Tags.SecurityIDSource, req.SecurityIdSource, defaultValue: SecurityIDSource.EXCHANGE_SYMBOL);
                SetIfHasValue(message, Tags.SecurityID, req.SecurityId, defaultValue: req.Symbol);

                // Side
                message.SetField(new CharField(Tags.Side, req.Side.getValue()));

                // Account
                SetIfHasValue(message, Tags.Account, req.Account);

                // ExecBroker
                SetIfHasValue(message, Tags.ExecBroker, req.ExecBroker);

                // ClOrdId
                SetIfHasValue(message, Tags.ClOrdID, req.ClOrdID, defaultValue: Guid.NewGuid().ToString("N"));

                // HandlInst
                if (req.IsDMA) {
                    message.SetField(new CharField(Tags.HandlInst, HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE));
                } else {
                    message.SetField(new CharField(Tags.HandlInst, HandlInst.MANUAL_ORDER));
                }

                // TimeInForce
                SetIfHasValue(message, Tags.TimeInForce, (char?)req.TimeInForce?.getValue(), defaultValue: TimeInForce.DAY);

                // Currency
                SetIfHasValue(message, Tags.Currency, req.Currency, "BRL");

                // Country
                SetIfHasValue(message, Tags.Country, req.Country);

                //OrdType
                SetIfHasValue(message, Tags.OrdType, req.OrdType?.getValue(), OrdType.LIMIT);

                // Qty
                SetIfHasValue(message, Tags.OrderQty, req.OrderQty);
                SetIfHasValue(message, Tags.MinQty, req.MinQty);
                SetIfHasValue(message, Tags.DisplayQty, req.DisplayQty);
                SetIfHasValue(message, Tags.StopPx, req.StopPx);

                // Price
                SetIfHasValue(message, Tags.Price, req.Price);

                // NoPartyIDs
                int i = 1;
                Group groupEnteringTrader = new Group(Tags.NoPartyIDs, i++);
                groupEnteringTrader.SetField(new IntField(Tags.PartyRole, PartyRole.ENTERING_TRADER));
                groupEnteringTrader.SetField(new CharField(Tags.PartyIDSource, PartyIDSource.PROPRIETARY_CUSTOM_CODE));
                groupEnteringTrader.SetField(new StringField(Tags.PartyID, req.Trader));
                message.AddGroup(groupEnteringTrader);
                if (req.ExecBroker.HasValue) {
                    Group groupEnteringFirm = new Group(Tags.NoPartyIDs, i++);
                    groupEnteringFirm.SetField(new IntField(Tags.PartyRole, PartyRole.ENTERING_FIRM));
                    groupEnteringFirm.SetField(new CharField(Tags.PartyIDSource, PartyIDSource.PROPRIETARY_CUSTOM_CODE));
                    groupEnteringFirm.SetField(new IntField(Tags.PartyID, req.ExecBroker.Value));
                    message.AddGroup(groupEnteringFirm);
                }

                _logger.LogInformation("Success on create message new order single");
                SendMessage(message);
            } catch (Exception ex) {
                _logger.LogError(ex, ex.Message);
                throw;
            }
        }

        public void OrderReplaceRequest(CreateOrderReplaceRequest req) {
            _logger.LogInformation("Success on create message new order single");
            try {
                if (req.ClientId == null && !string.IsNullOrEmpty(_defaultClientId)) {
                    req.ClientId = _defaultClientId;
                }
                EvaluateResult evaluateResult = req.Evaluate();
                if (evaluateResult.HasIssue()) {
                    throw new ArgumentNullException(nameof(req), evaluateResult.GetIssue());
                }

                Message message = BuildMessage(MsgType.ORDERCANCELREPLACEREQUEST);

                // ClientId
                SetIfHasValue(message, Tags.ClientID, req.ClientId);

                // Security settings
                SetIfHasValue(message, Tags.SecurityExchange, req.ExDestination);
                SetIfHasValue(message, Tags.SecurityIDSource, req.SecurityIdSource);
                SetIfHasValue(message, Tags.SecurityID, req.SecurityId);

                // Side
                message.SetField(new CharField(Tags.Side, req.Side.getValue()));

                //OrigClOrdID
                message.SetField(new StringField(Tags.OrigClOrdID, req.OrigClOrdID));

                // ClOrdId
                SetIfHasValue(message, Tags.ClOrdID, req.ClOrdID, defaultValue: Guid.NewGuid().ToString("N"));

                // TimeInForce
                SetIfHasValue(message, Tags.TimeInForce, req.TimeInForce?.getValue());

                // CCY
                SetIfHasValue(message, Tags.Currency, req.Currency);

                // Country
                SetIfHasValue(message, Tags.Country, req.Country);

                //OrdType
                SetIfHasValue(message, Tags.OrdType, req.OrdType?.getValue());

                // Qty
                SetIfHasValue(message, Tags.OrderQty, req.OrderQty);
                SetIfHasValue(message, Tags.MinQty, req.MinQty);
                SetIfHasValue(message, Tags.DisplayQty, req.DisplayQty);
                SetIfHasValue(message, Tags.StopPx, req.StopPx);

                // Price
                SetIfHasValue(message, Tags.Price, req.Price);

                SendMessage(message);
                _logger.LogInformation("Success on create message order replace request");
            } catch (Exception ex) {
                _logger.LogError(ex, ex.Message);
                throw;
            }
        }

        public void OrderCancelRequest(CreateOrderCancelRequest req) {
            _logger.LogInformation("Creating message order cancel request");
            try {
                if (req.ClientId == null && !string.IsNullOrEmpty(_defaultClientId)) {
                    req.ClientId = _defaultClientId;
                }
                EvaluateResult evaluateResult = req.Evaluate();
                if (evaluateResult.HasIssue()) {
                    throw new ArgumentNullException(nameof(req), evaluateResult.GetIssue());
                }

                Message message = BuildMessage(MsgType.ORDERCANCELREQUEST);

                // ClOrdId
                SetIfHasValue(message, Tags.ClOrdID, req.ClOrdID, defaultValue: Guid.NewGuid().ToString("N"));

                // ClientId
                SetIfHasValue(message, Tags.ClientID, req.ClientId);

                //OrderID
                SetIfHasValue(message, Tags.OrderID, req.OrderID);

                //OrigClOrdID
                SetIfHasValue(message, Tags.OrigClOrdID, req.OrigClOrdID);

                // Side
                message.SetField(new CharField(Tags.Side, req.Side.getValue()));

                // OrderQty is required if HandlInst is MANUAL_ORDER (care)
                SetIfHasValue(message, Tags.OrderQty, req.OrderQty);

                SendMessage(message);
                _logger.LogInformation("Success Create message order cancel request");
            } catch (Exception ex) {
                _logger.LogError(ex, ex.Message);
                throw;
            }
        }

        private void HandlerMessage(Message message) {

            switch (message.Header.GetString(Tags.MsgType)) {
                //35=3
                case MsgType.REJECT:
                    Task.Run(() => { _subjectObservableReject.OnNext(new ObservableReject(message)); });
                    break;
                //35=8
                case MsgType.EXECUTION_REPORT:
                    Task.Run(() => { _subjectExecutionReport.OnNext(new ObservableExecutionReport(message)); });
                    break;
                //35=9
                case MsgType.ORDERCANCELREJECT:
                    Task.Run(() => { _subjectOrderCancelReject.OnNext(new ObservableOrderCancelReject(message)); });
                    break;
                //35=J
                case MsgType.BUSINESS_MESSAGE_REJECT:
                    Task.Run(() => { _subjectBusinessMessageReject.OnNext(new ObservableBusinessMessageReject(message)); });
                    break;
            }

            Task.Run(() => { _subjectMessage.OnNext(message); });
        }

        private void OnSessionChange(Tuple<SessionID, bool> sessionIdIsActiveTuple) {
            if (_defaultSession != null && _defaultSession.SessionID == sessionIdIsActiveTuple.Item1) {
                _isConnect = sessionIdIsActiveTuple.Item2;

                if (_isConnect) {
                    _autoResetEvent.Set();
                }
            }
            Task.Run(() => {
                _subjectSessionIsActive.OnNext(sessionIdIsActiveTuple);
            });
        }

        private void SetObservable() {
            ObservableMessage = _app.ObservableMessage;
            ObservableSessionIsAlive = _app.ObservableSessionIsActive;

            _app.ObservableSessionIsActive.Subscribe(OnSessionChange);
            _app.ObservableMessage.Subscribe(HandlerMessage);

            _subjectMessage = new Subject<Message>();
            _subjectSessionIsActive = new Subject<Tuple<SessionID, bool>>();

            _subjectExecutionReport = new Subject<ObservableExecutionReport>();
            _subjectOrderCancelReject = new Subject<ObservableOrderCancelReject>();
            _subjectBusinessMessageReject = new Subject<ObservableBusinessMessageReject>();
            _subjectObservableReject = new Subject<ObservableReject>();

            ObservableExecutionReport = _subjectExecutionReport.AsObservable();
            ObservableOrderCancelReject = _subjectOrderCancelReject.AsObservable();
            ObservableBusinessMessageReject = _subjectBusinessMessageReject.AsObservable();
            ObservableReject = _subjectObservableReject.AsObservable();
        }

        private void SetSessionId() {
            _logger.LogWarning("Connecting...");

            HashSet<SessionID> sessionlist = _app.GetSessionIDs();
            if (sessionlist is null ||
                sessionlist.Count <= 0) {
                throw new NullReferenceException("BTG Solution session not created");
            }
            SessionID session = sessionlist.Where(x => x.BeginString == _settings.FixSettings.BeginString &&
                                                 x.TargetCompID == _settings.TargetCompID &&
                                                 x.SenderCompID == _settings.SenderCompID).FirstOrDefault()
                                                 ?? throw new NullReferenceException("BTG Solution session not created");

            _defaultSession = Session.LookupSession(session);

            _autoResetEvent.WaitOne(TimeSpan.FromSeconds(15));
            if (!_isConnect) {
                throw new InvalidOperationException("Not connection");
            }
        }

        private void SendMessage(Message message) {
            if (_defaultSession == null || !_isConnect) {
                throw new NullReferenceException("Session not connected");
            }

            if (message == null) {
                throw new NullReferenceException(nameof(message));
            }

            _defaultSession.Send(message);
        }

        private void SetIfHasValue(Message message, int tag, string value, string defaultValue = null) {
            if (!string.IsNullOrEmpty(value)) {
                message.SetField(new StringField(tag, value));
            } else if (defaultValue != null) {
                message.SetField(new StringField(tag, defaultValue));
            }
        }

        private void SetIfHasValue(Message message, int tag, int? value, int? defaultValue = null) {
            if (value.HasValue) {
                message.SetField(new IntField(tag, value.Value));
            } else if (defaultValue.HasValue) {
                message.SetField(new IntField(tag, defaultValue.Value));
            }
        }

        private void SetIfHasValue(Message message, int tag, char? value, char? defaultValue = null) {
            if (value.HasValue) {
                message.SetField(new CharField(tag, value.Value));
            } else if (defaultValue.HasValue) {
                message.SetField(new CharField(tag, defaultValue.Value));
            }
        }

        private void SetIfHasValue(Message message, int tag, decimal? value, decimal? defaultValue = null) {
            if (value.HasValue) {
                message.SetField(new DecimalField(tag, value.Value));
            } else if (defaultValue.HasValue) {
                message.SetField(new DecimalField(tag, defaultValue.Value));
            }
        }

        private static Message BuildMessage(string messageType) {
            Message message = new Message();
            message.Header.SetField(new MsgType(messageType));

            // TransactTime
            message.SetField(new DateTimeField(Tags.TransactTime, DateTime.UtcNow));
            return message;
        }
              
    }
}