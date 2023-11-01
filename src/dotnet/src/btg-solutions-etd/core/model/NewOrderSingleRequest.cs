namespace BtgPactualSolutions.Etd.core.model {
    using System;
    using System.Collections.Generic;
    using QuickFix.Fields;
    public class CreateNewOrderSingleRequest {
        public CreateNewOrderSingleRequest() {
            _validations = new List<string>();
        }
        public int? Account { get; set; }
        public int? ExecBroker { get; set; }
        public string ClOrdID { get; set; }

        // sets HandleInst 
        public bool IsDMA { get; set; }

       
        public string ClientId { get; set; }
        public decimal? Price { get; set; }
        public string Symbol { get; set; }

        public string ExDestination { get; set; }
        public string SecurityId { get; set; }
        public string SecurityIdSource { get; set; }

        public string Currency { get; set; }
        public string Country { get; set; }

        public decimal? OrderQty { get; set; }
        public decimal? MinQty { get; set; }
        public decimal? DisplayQty { get; set; }
        public decimal? StopPx { get; set; }

        public string Trader { get; set; }

        public Side Side { get; set; }
        public OrdType OrdType { get; set; }
        public TimeInForce TimeInForce { get; set; }

        private List<string> _validations;
        public EvaluateResult Evaluate() {
            _validations.Clear();
            if (string.IsNullOrEmpty(ClientId)) {
                _validations.Add("ClientId is required");
            }

            if (Side == null) {
                _validations.Add("Side is required");
            }

            if (!OrderQty.HasValue) {
                _validations.Add("OrderQty is required");
            }

            if (string.IsNullOrEmpty(Trader)) {
                _validations.Add("Trader is required");
            }

            if (string.IsNullOrEmpty(Symbol)) {
                _validations.Add("Symbol is required");
            }

            if (IsDMA) {
                if (!Account.HasValue) {
                    _validations.Add("Account is required since is DMA");
                }
                if (!ExecBroker.HasValue) {
                    _validations.Add("ExecBroker is required since is DMA");
                }
            }

            // Validation to avoid unintentional order Market
            if (OrdType == null && !Price.HasValue) {
                _validations.Add("Price or OrdType is required, This is a validation to avoid unintentional Market order, if that is your intention, please set ordType as Market!");
            }

            if (OrdType != null) {
                if (OrdType.getValue() == OrdType.LIMIT && !Price.HasValue) {
                    _validations.Add("Price is required if OrdType is Limit");
                }
                else if (OrdType.getValue() == OrdType.MARKET && Price.HasValue) {
                    _validations.Add("Price should be null if OrdType is Market");
                }
            }
            return new EvaluateResult(string.Join(", ", _validations));
        }
    }
}