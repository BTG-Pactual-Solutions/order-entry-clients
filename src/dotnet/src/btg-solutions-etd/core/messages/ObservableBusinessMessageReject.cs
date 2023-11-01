namespace BtgPactualSolutions.Etd.core.messages { 
    using QuickFix.Fields;
    using QuickFix;
    using System;
    public class ObservableBusinessMessageReject {
        public string msgType { get; set; }
        public string businessRejectReason { get; private set; }
        public string clOrdID { get; private set; }
        public string text { get; private set; }
        public string refMsgType { get; private set; }

        public ObservableBusinessMessageReject(Message msg) {
            if (msg == null) {
                throw new NullReferenceException(nameof(msg));
            }
            msgType = msg.Header.TryGetValue(Tags.MsgType, out string value) ? value : string.Empty;
            businessRejectReason = msg.TryGetValue(Tags.BusinessRejectReason, out value) ? value : string.Empty;
            clOrdID = msg.TryGetValue(Tags.ClOrdID, out value) ? value : string.Empty;
            text = msg.TryGetValue(Tags.Text, out value) ? value : string.Empty;
            refMsgType = msg.TryGetValue(Tags.RefMsgType, out value) ? value : string.Empty;
        }
    }
}
