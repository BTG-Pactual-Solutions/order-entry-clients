namespace BtgPactualSolutions.Etd.core {
    using Microsoft.Extensions.Logging;
    using System.Reactive.Subjects;
    using System.Reactive.Linq;
    using QuickFix.Fields;
    using QuickFix;
    using System;
    using BtgPactualSolutions.Etd.core.settings;
    using QuickFix.Transport;
    using System.Collections.Generic;

    internal class BtgSolutionsFixConnection : IBtgSolutionsFixConnection {
        private readonly ILogger _logger;
        private readonly IInitiator _fixConnection;
        private readonly InitiatorSettings _settings;

        private readonly ISubject<Message> _subjectMessage;
        private readonly ISubject<Tuple<SessionID, bool>> _subjectSessionIsActive;
        private readonly string _username;
        private readonly string _password;

        public IObservable<Message> ObservableMessage { get; private set; }
        public IObservable<Tuple<SessionID, bool>> ObservableSessionIsActive { get; private set; }

        public BtgSolutionsFixConnection(ILogger logger, string senderCompID, string targetCompID, string socketConnectHost, long socketConnectPort, string SSLCertificate, string userName, string password, FixSettings settingsParametter = null) {
            _username = userName;
            _password = password;

            _logger = logger;


            _settings = new InitiatorSettings();


            var sessionSettings = SetSettings(senderCompID, targetCompID, socketConnectHost, socketConnectPort, SSLCertificate, settingsParametter);
            _fixConnection = new SocketInitiator(this, new FileStoreFactory(sessionSettings), sessionSettings, new FileLogFactory(sessionSettings));


            _subjectMessage = new Subject<Message>();
            ObservableMessage = _subjectMessage.AsObservable();

            _subjectSessionIsActive = new Subject<Tuple<SessionID, bool>>();
            ObservableSessionIsActive = _subjectSessionIsActive.AsObservable();

        }

        public void Start() {
            _fixConnection.Start();
        }

        public void Stop() {
            _fixConnection.Stop();
        }

        public HashSet<SessionID> GetSessionIDs() {
            HashSet<SessionID> result = _fixConnection.GetSessionIDs();
            return result;
        }
        public InitiatorSettings GetInitiatorSettings() {
            return _settings;
        }

        public void FromAdmin(Message message, SessionID sessionID) {
            _logger.LogInformation($"FromAdmin {sessionID}");
            _subjectMessage.OnNext(message);
        }

        public void FromApp(Message message, SessionID sessionID) {
            _logger.LogInformation($"FromApp {sessionID}");

            _subjectMessage.OnNext(message);
        }

        public void OnCreate(SessionID sessionID) {
            _logger.LogInformation($"OnCreate {sessionID}");
        }

        public void OnLogon(SessionID sessionID) {
            _logger.LogInformation($"OnLogon {sessionID}");
            _subjectSessionIsActive.OnNext(new Tuple<SessionID, bool>(sessionID, true));
        }

        public void OnLogout(SessionID sessionID) {
            _logger.LogInformation($"OnLogout {sessionID}");
            _subjectSessionIsActive.OnNext(new Tuple<SessionID, bool>(sessionID, false));
        }

        public void ToAdmin(Message message, SessionID sessionID) {
            _logger.LogInformation($"ToAdmin message {message} | sessionID: {sessionID}");

            if (message.Header.GetString(Tags.MsgType) == MsgType.LOGON) {
                if (!(string.IsNullOrEmpty(_username) && string.IsNullOrEmpty(_password))) {
                    message.SetField(new StringField(Tags.Username, _username));
                    message.SetField(new StringField(Tags.Password, _password));
                }
            }

            _subjectMessage.OnNext(message);
        }

        public void ToApp(Message message, SessionID sessionID) {
            _logger.LogInformation($"ToApp message {message} | sessionID: {sessionID}");
        }

        private SessionSettings SetSettings(string senderCompID, string targetCompID, string socketConnectHost, long socketConnectPort, string SSLCertificate, FixSettings settingsParametter = null) {

            _settings.SetAppSettings(settingsParametter);
            _settings.SenderCompID = senderCompID;
            _settings.TargetCompID = targetCompID;
            _settings.SocketConnectHost = socketConnectHost;
            _settings.SocketConnectPort = socketConnectPort;
            _settings.SSLCertificate = SSLCertificate;

            FixSettings fixSettings = _settings.FixSettings;
            SessionSettings settings = SetupSessionSettings(fixSettings);

            Dictionary sessionDictionary = new Dictionary();
            SessionID sessionId = new SessionID(fixSettings.BeginString, _settings.SenderCompID, _settings.TargetCompID);

            sessionDictionary.SetString(SessionSettings.CONNECTION_TYPE, fixSettings.ConnectionType);
            sessionDictionary.SetString(SessionSettings.BEGINSTRING, fixSettings.BeginString);
            sessionDictionary.SetString(SessionSettings.SENDERCOMPID, _settings.SenderCompID);
            sessionDictionary.SetString(SessionSettings.TARGETCOMPID, _settings.TargetCompID);
            SetupDialect(sessionDictionary);
            sessionDictionary.SetString(SessionSettings.SOCKET_CONNECT_HOST, _settings.SocketConnectHost.ToString());
            sessionDictionary.SetLong(SessionSettings.SOCKET_CONNECT_PORT, _settings.SocketConnectPort);
            SetupSsl(sessionDictionary);

            settings.Set(sessionId, sessionDictionary);
            return settings;
        }

        private static SessionSettings SetupSessionSettings(FixSettings fixSettings) {
            SessionSettings settings = new SessionSettings();

            Dictionary sessionDictionary = new Dictionary();
            sessionDictionary.SetLong(SessionSettings.RECONNECT_INTERVAL, fixSettings.ReconnectInterval);
            sessionDictionary.SetString(SessionSettings.FILE_STORE_PATH, fixSettings.FileStorePath);
            sessionDictionary.SetString(SessionSettings.FILE_LOG_PATH, fixSettings.FileLogPath);
            sessionDictionary.SetString(SessionSettings.START_TIME, fixSettings.StartTime.ToString());
            sessionDictionary.SetString(SessionSettings.END_TIME, fixSettings.EndTime.ToString());
            sessionDictionary.SetLong(SessionSettings.HEARTBTINT, fixSettings.HeartBtInt);
            sessionDictionary.SetLong(SessionSettings.LOGOUT_TIMEOUT, fixSettings.LogoutTimeout);
            sessionDictionary.SetString(SessionSettings.RESET_ON_LOGON, fixSettings.ResetOnLogon.ToString());
            sessionDictionary.SetString(SessionSettings.RESET_ON_DISCONNECT, fixSettings.ResetOnDisconnect.ToString());
            sessionDictionary.SetString(SessionSettings.VALIDATE_USER_DEFINED_FIELDS, "N");
            sessionDictionary.SetString(SessionSettings.VALIDATE_FIELDS_HAVE_VALUES, "N");
            sessionDictionary.SetString(SessionSettings.VALIDATE_FIELDS_OUT_OF_ORDER, "N");
            sessionDictionary.SetString(SessionSettings.VALIDATE_LENGTH_AND_CHECKSUM, "N");
            sessionDictionary.SetString(SessionSettings.ALLOW_UNKNOWN_MSG_FIELDS, "Y");

            settings.Set(sessionDictionary);

            return settings;
        }

        private void SetupDialect(Dictionary sessionDictionary) {
            if (string.IsNullOrEmpty(_settings.FixSettings.Dialect)) {
                sessionDictionary.SetString(SessionSettings.USE_DATA_DICTIONARY, "N");
            } else {
                sessionDictionary.SetString(SessionSettings.USE_DATA_DICTIONARY, "Y");
                sessionDictionary.SetString(SessionSettings.DATA_DICTIONARY, _settings.FixSettings.Dialect);
            }
        }

        private void SetupSsl(Dictionary sessionDic) {
            if (!string.IsNullOrEmpty(_settings.SSLCertificate)) {
                sessionDic.SetString(SessionSettings.SSL_CERTIFICATE, _settings.SSLCertificate);
                sessionDic.SetString(SessionSettings.SSL_ENABLE, "Y");
            }
            sessionDic.SetString(SessionSettings.SSL_VALIDATE_CERTIFICATES, "N");
        }
    }
}