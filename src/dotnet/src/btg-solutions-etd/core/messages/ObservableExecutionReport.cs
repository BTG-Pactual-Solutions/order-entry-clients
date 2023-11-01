namespace BtgPactualSolutions.Etd.core.messages {
    using QuickFix.Fields;
    using QuickFix;
    using System;

    public class ObservableExecutionReport {
        public string MsgType { get; private set; }
        public string OrdStatus { get; private set; }
        public string ClOrdID { get; private set; }
        public string OrigClOrdID { get; private set; }
        public string OrderID { get; private set; }
        public decimal? OrderQty { get; private set; }
        public Side Side { get; private set; }
        public OrdType OrdType { get; private set; }
        public decimal? AvgPx { get; private set; }
        public decimal? LeavesQty { get; private set; }
        public decimal? CumQty { get; private set; }
        public string Symbol { get; private set; }
        public string SenderSubID { get; private set; }
        public string Account { get; private set; }
        public string ClientID { get; private set; }

        public string EnteringTrader { get; private set; }
        public string InternalID { get; private set; }
        public string ExecBroker { get; private set; }

        public string TransactTime { get; private set; }
        public decimal? StopPx { get; private set; }
        public decimal? Price { get; private set; }
        public string Memo { get; private set; }
        public string Currency { get; private set; }
        public string PositionEffect { get; private set; }
        public string WorkingIndicator { get; private set; }
        public string ContractMultiplier { get; private set; }
        public string MaxFloor { get; private set; }
        public string MinQty { get; private set; }
        public string ExecType { get; private set; }
        public string ExecID { get; private set; }
        public string Text { get; private set; }
        public TimeInForce TimeInForce { get; private set; }
        public HandlInst HandlInst { get; private set; }

        public ObservableExecutionReport(Message msg) {
            if (msg == null) {
                throw new NullReferenceException(nameof(msg));
            }
            MsgType = msg.Header.TryGetValue(Tags.MsgType, out string value) ? value : string.Empty;
            OrdStatus = GetAsString(msg, Tags.OrdStatus);
            ClOrdID = GetAsString(msg, Tags.ClOrdID);
            OrigClOrdID = GetAsString(msg, Tags.OrigClOrdID);
            OrderID = GetAsString(msg, Tags.OrderID);
            OrderQty = GetAsLong(msg, Tags.OrderQty);
          
            if (msg.TryGetValue(Tags.Side, out char side)) {
                Side = new Side(side);
            }
            if (msg.TryGetValue(Tags.OrdType, out char type)) {
                OrdType = new OrdType(type);
            }
            AvgPx = GetAsDecimal(msg, Tags.AvgPx);
            LeavesQty = GetAsLong(msg, Tags.LeavesQty);
            CumQty = GetAsLong(msg, Tags.CumQty);
            Symbol = msg.TryGetValue(Tags.Symbol, out value) ? value : string.Empty;
            SenderSubID = msg.Header.TryGetValue(Tags.SenderSubID, out value) ? value : string.Empty;


            Account = msg.TryGetValue(Tags.Account, out value) ? value : string.Empty;
            ClientID = msg.TryGetValue(Tags.ClientID, out value) ? value : string.Empty;

            for (int i = 1; i <= msg.GroupCount(Tags.NoPartyIDs); i++) {

                Group group = msg.GetGroup(i, Tags.NoPartyIDs);

                group.TryGetValue(Tags.PartyID, out string partyId);
                group.TryGetValue(Tags.PartyRole, out int partyRole);

                switch (partyRole) {
                    case PartyRole.ENTERING_TRADER:
                        EnteringTrader = partyId;
                        break;

                    case PartyRole.INTERESTED_PARTY:
                        InternalID = partyId;
                        break;

                    case PartyRole.ENTERING_FIRM:
                        ExecBroker = partyId;
                        break;
                }
            }

            TransactTime = msg.TryGetValue(Tags.TransactTime, out value) ? value : string.Empty;
            Price = GetAsDecimal(msg, Tags.Price);
            StopPx = GetAsDecimal(msg, Tags.StopPx);
            Memo = msg.TryGetValue(5149, out value) ? value : value;
            Currency = GetAsString(msg, Tags.Currency);
            PositionEffect = msg.TryGetValue(Tags.PositionEffect, out value) ? value : string.Empty;
            WorkingIndicator = msg.TryGetValue(Tags.WorkingIndicator, out value) ? value : string.Empty;
            ContractMultiplier = msg.TryGetValue(Tags.ContractMultiplier, out value) ? value : string.Empty;
            MaxFloor = msg.TryGetValue(Tags.MaxFloor, out value) ? value : string.Empty;
            MinQty = msg.TryGetValue(Tags.MinQty, out value) ? value : string.Empty;
            ExecType = msg.TryGetValue(Tags.ExecType, out value) ? value : string.Empty;
            ExecID = msg.TryGetValue(Tags.ExecID, out value) ? value : string.Empty;
            Text = msg.TryGetValue(Tags.Text, out value) ? value : string.Empty;

            if (msg.TryGetValue(Tags.TimeInForce, out char timeInForce)) {
                TimeInForce = new TimeInForce(timeInForce);
            }
        }

        private decimal? GetAsDecimal(Message msg, int fieldTag) {
            if(msg.TryGetValue(fieldTag, out decimal value)) {
                return value;
            }
            return null;
        }

        private string GetAsString(Message msg, int fieldTag) {
            return msg.TryGetValue(fieldTag, out string value) ? value : string.Empty;
        }

        private long? GetAsLong(Message msg, int fieldTag) {
            if(msg.TryGetValue(fieldTag, out long value)) {
                return value;
            } 
            return null;
        }
    }
}
