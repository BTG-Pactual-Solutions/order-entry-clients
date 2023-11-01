namespace BtgPactualSolutions.Etd.core.model {
    public class EvaluateResult {
        private string _issue;
        internal EvaluateResult(string issue) {
            _issue = issue;
        }

        public bool HasIssue() {
            return !string.IsNullOrEmpty(_issue);
        }

        public string GetIssue() {
            return _issue;
        }
    }
}
