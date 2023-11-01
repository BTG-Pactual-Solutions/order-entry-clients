namespace BtgPactualSolutions.Etd.core.messages {
    using QuickFix.Fields;
    using QuickFix;
    using System;
    public class ObservableOrderCancelReject {
        public string msgType { get; private set; }
        public string execType { get; private set; }
        public string ordStatus { get; private set; }
        public string orderID { get; private set; }
        public string clOrdID { get; private set; }
        public string origClOrdID { get; private set; }
        public string cxlRejResponseTo { get; set; }
        public string text { get; private set; }
        public string account { get; private set; }
        public string symbol { get; private set; }
        public string noPartyIDss { get; private set; }

        public ObservableOrderCancelReject(Message msg) {
            if (msg == null) {
                throw new NullReferenceException(nameof(msg));
            }

            msgType = msg.Header.TryGetValue(Tags.MsgType, out string value) ? value : string.Empty;

            execType = msg.TryGetValue(Tags.ExecType, out value) ? value : string.Empty;
            ordStatus = msg.TryGetValue(Tags.OrdStatus, out value) ? value : string.Empty;
            orderID = msg.TryGetValue(Tags.OrderID, out value) ? value : string.Empty;
            clOrdID = msg.TryGetValue(Tags.ClOrdID, out value) ? value : string.Empty;
            origClOrdID = msg.TryGetValue(Tags.OrigClOrdID, out value) ? value : string.Empty;
            cxlRejResponseTo = msg.TryGetValue(Tags.CxlRejResponseTo, out value) ? value : string.Empty;
            text = msg.TryGetValue(Tags.Text, out value) ? value : string.Empty;
            account = msg.TryGetValue(Tags.Account, out value) ? value : string.Empty;
            symbol = msg.TryGetValue(Tags.Symbol, out value) ? value : string.Empty;
        }
    }
}
