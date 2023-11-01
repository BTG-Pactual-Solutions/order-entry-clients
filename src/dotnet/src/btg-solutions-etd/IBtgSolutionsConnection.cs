namespace BtgPactualSolutions.Etd {
    using BtgPactualSolutions.Etd.core.messages;
    using BtgPactualSolutions.Etd.core.model;
    using QuickFix;
    using System;

    public interface IBtgSolutionsConnection {
        /// <summary>
        /// Get if solutions is connected
        /// </summary>
        bool IsConnected { get; }
        
        /// <summary>
        /// Start solutions connection
        /// </summary>
        void Start();

        /// <summary>
        /// Stop solutions connection
        /// </summary>
        void Stop();

        /// <summary>
        /// Send custom fix message
        /// 
        /// throw NullReferenceException
        /// </summary>
        /// <param name="message"></param>
        void SendFixMessage(Message message);

        /// <summary>
        /// Send new order
        /// </summary>
        /// <param name="newOrderSingleRequest"></param>
        void NewOrderSingle(CreateNewOrderSingleRequest newOrderSingleRequest);

        /// <summary>
        /// Send order replace
        /// </summary>
        /// <param name="dto"></param>
        void OrderReplaceRequest(CreateOrderReplaceRequest dto);

        /// <summary>
        /// Send order cancel
        /// </summary>
        /// <param name="dto"></param>
        void OrderCancelRequest(CreateOrderCancelRequest dto);

        /// <summary>
        /// Observe fix message
        /// </summary>
        IObservable<Message> ObservableMessage { get; }

        /// <summary>
        /// Observe parsed execution report
        /// </summary>
        IObservable<ObservableExecutionReport> ObservableExecutionReport { get; }

        /// <summary>
        /// Observe parsed order cancel reject
        /// </summary>
        IObservable<ObservableOrderCancelReject> ObservableOrderCancelReject { get; }

        /// <summary>
        /// Observe parsed business reject
        /// </summary>
        IObservable<ObservableBusinessMessageReject> ObservableBusinessMessageReject { get; }

        /// <summary>
        /// Observe parsed reject
        /// </summary>
        IObservable<ObservableReject> ObservableReject { get; }

        /// <summary>
        /// Observe connection status change
        /// 
        /// if true session is connected
        /// </summary>
        IObservable<Tuple<SessionID, bool>> ObservableSessionIsAlive { get; }
    }
}