namespace BtgPactualSolutions.Etd.core.settings {
    public class InitiatorSettings {
        public InitiatorSettings() {
            FixSettings = new FixSettings();
        }

        public string SenderCompID { get; set; }
        public string TargetCompID { get; set; }
        public string SocketConnectHost { get; set; }
        public long SocketConnectPort { get; set; }
        public string SSLCertificate { get; set; }
        public FixSettings FixSettings { get; private set; }


        public void SetAppSettings(FixSettings app = null) {
            app = app ?? new FixSettings();

            FixSettings.ReconnectInterval = app.ReconnectInterval;
            FixSettings.FileStorePath = app.FileStorePath;
            FixSettings.FileLogPath = app.FileLogPath;
            FixSettings.StartTime = app.StartTime;
            FixSettings.EndTime = app.EndTime;
            FixSettings.HeartBtInt = app.HeartBtInt;
            FixSettings.LogoutTimeout = app.LogoutTimeout;
            FixSettings.ResetOnLogon = app.ResetOnLogon;
            FixSettings.ResetOnLogout = app.ResetOnLogout;
            FixSettings.ResetOnDisconnect = app.ResetOnDisconnect;
            FixSettings.Dialect = app.Dialect;
        }
    }
}
