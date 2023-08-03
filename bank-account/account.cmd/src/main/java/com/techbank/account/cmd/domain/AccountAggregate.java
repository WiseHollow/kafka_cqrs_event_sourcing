package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;

    private double balance;

    /**
     * Constructor used to open a new Account Aggregate
     * @param command OpenAccountCommand with account aggregate details
     */
    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .dateCreated(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    /**
     * Apply changes from the event to this aggregate.
     * @param event AccountOpenedEvent
     */
    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    /**
     * Add funds to this account aggregate.
     * @param amount Amount to add to the account aggregate balance
     */
    public void depositFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Funds cannot be deposited into a closed bank account.");
        } else if (amount <= 0) {
            throw new IllegalStateException("The deposited amount must be greater than 0.");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    /**
     * Apply changes from the event to this aggregate.
     * @param event FundsDepositedEvent
     */
    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    /**
     * Remove funds from this account aggregate.
     * @param amount Amount to remove from the account aggregate balance
     */
    public void withdrawFunds(double amount) {
        if (!active) {
            throw new IllegalStateException("Funds cannot be withdrawn into a closed bank account.");
        } else if (amount <= 0) {
            throw new IllegalStateException("The withdrawn amount must be greater than 0.");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    /**
     * Apply changes from the event to this aggregate.
     * @param event FundsWithdrawnEvent
     */
    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    /**
     * Close the account aggregate. An already closed account will throw an exception if another close is attempted.
     * @throws IllegalStateException If account is already closed
     */
    public void closeAccount() {
        if (!active) {
            throw new IllegalStateException("The bank account is already closed.");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    /**
     * Apply changes from the event to this aggregate.
     * @param event AccountClosedEvent
     */
    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }
}
