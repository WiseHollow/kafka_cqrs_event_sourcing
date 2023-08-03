package com.techbank.account.cmd.api.commands;

public interface CommandHandler {

    /**
     * Handle a OpenAccountCommand instance by constructing a new Aggregate and saving it with an EventSourcingHandler
     * @param command OpenAccountCommand
     */
    void handle(OpenAccountCommand command);
    /**
     * Handle a DepositFundsCommand instance by retrieving the related Aggregate, depositing the funds, and saving with
     * an EventSourcingHandler.
     * @param command DepositFundsCommand
     */
    void handle(DepositFundsCommand command);
    /**
     * Handle a WithdrawFundsCommand instance by retrieving the related Aggregate, withdrawing the funds, and saving
     * with an EventSourcingHandler.
     * @param command WithdrawFundsCommand
     */
    void handle(WithdrawFundsCommand command);
    /**
     * Handle a CloseAccountCommand instance by retrieving the related Aggregate, setting the account as not active,
     * and saving with an EventSourcingHandler.
     * @param command CloseAccountCommand
     */
    void handle(CloseAccountCommand command);
    /**
     * Handle a RestoreReadDbCommand instance by republishing all recorded events in the Event Store.
     * @param command RestoreReadDbCommand
     */
    void handle(RestoreReadDbCommand command);
}
