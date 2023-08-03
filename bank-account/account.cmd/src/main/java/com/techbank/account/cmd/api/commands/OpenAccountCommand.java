package com.techbank.account.cmd.api.commands;

import com.techbank.account.common.dtos.AccountType;
import com.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {

    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
