namespace BtgPactualSolutions.Etd.core.messages {
    using QuickFix.Fields;
    using QuickFix;
    using System;

    public class ObservableReject {
        public string msgType { get; private set; }
        public string clOrdID { get; private set; }
        public string refSeqNum { get; private set; }
        public string refMsgType { get; private set; }
        public string refTagID { get; private set; }
        public string sessionRejectReason { get; private set; }
        public string Text { get; private set; }

        public ObservableReject(Message msg) {
            if (msg == null) {
                throw new NullReferenceException(nameof(msg));
            }
            msgType = msg.Header.TryGetValue(Tags.MsgType, out string value) ? value : string.Empty;

            clOrdID = msg.TryGetValue(Tags.ClOrdID, out value) ? value : string.Empty;
            refSeqNum = msg.TryGetValue(Tags.RefSeqNum, out value) ? value : string.Empty;
            refMsgType = msg.TryGetValue(Tags.RefMsgType, out value) ? value : string.Empty;
            refTagID = msg.TryGetValue(Tags.RefTagID, out value) ? value : string.Empty;
            sessionRejectReason = msg.TryGetValue(Tags.SessionRejectReason, out value) ? value : string.Empty;
            Text = msg.TryGetValue(Tags.Text, out value) ? value : string.Empty;
        }
    }
}

