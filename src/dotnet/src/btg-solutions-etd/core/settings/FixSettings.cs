namespace BtgPactualSolutions.Etd.core.settings {
    using System;

    public class FixSettings {
        public int ReconnectInterval { get; set; } = 10;
        public string FileStorePath { get; set; } = "store";
        public string FileLogPath { get; set; } = "log";
        public TimeSpan StartTime { get; set; } = TimeSpan.Parse("00:00:01");
        public TimeSpan EndTime { get; set; } = TimeSpan.Parse("23:59:59");
        public int HeartBtInt { get; set; } = 30;
        public int LogoutTimeout { get; set; } = 5;
        public char ResetOnLogon { get; set; } = 'Y';
        public char ResetOnLogout { get; set; } = 'Y';
        public char ResetOnDisconnect { get; set; } = 'Y';


        public string ConnectionType { get; private set; } = "initiator";
        public string BeginString { get; private set; } = "FIX.4.4";

        public string Dialect { get; set; }
    }
}