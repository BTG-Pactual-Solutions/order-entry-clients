namespace BtgPactualSolutions.Etd.core {
    using BtgPactualSolutions.Etd.core.settings;
    using QuickFix;
    using System;
    using System.Collections.Generic;

    public interface IBtgSolutionsFixConnection : IApplication {
        IObservable<Message> ObservableMessage { get; }
        IObservable<Tuple<SessionID, bool>> ObservableSessionIsActive { get; }

        InitiatorSettings GetInitiatorSettings();

        HashSet<SessionID> GetSessionIDs();
        void Start();
        void Stop();
    }
}
