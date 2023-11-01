namespace BtgPactualSolutions.Etd.core.model {
    using QuickFix.Fields;
    using System.Collections.Generic;

    public class CreateOrderCancelRequest {
        public CreateOrderCancelRequest() {
            _validations = new List<string>();
        }
        public string ClientId { get; set; }
        public string ClOrdID { get; set; }
        public string OrigClOrdID { get; set; }
        public string OrderID { get; set; }
        public decimal? OrderQty { get; set; }
        public Side Side { get; set; }

        private List<string> _validations;
        public EvaluateResult Evaluate() {
            _validations.Clear();
            if (string.IsNullOrEmpty(ClientId)) {
                _validations.Add("ClientId is required");
            }

            if (string.IsNullOrEmpty(OrderID) && string.IsNullOrEmpty(OrigClOrdID)) {
                _validations.Add("OrderID or OrigClOrdID is required");
            }

            if (Side == null) {
                _validations.Add("Side is required");
            }
            return new EvaluateResult(string.Join(", ", _validations));
        }
    }
}