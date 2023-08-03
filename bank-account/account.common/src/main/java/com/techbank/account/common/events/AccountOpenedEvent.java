package com.techbank.account.common.events;

import com.techbank.account.common.dtos.AccountType;
import com.techbank.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountOpenedEvent extends BaseEvent {

    private String accountHolder;
    private AccountType accountType;
    private Date dateCreated;
    private double openingBalance;
}
