package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.common.dtos.BaseResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.infastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/withdrawFunds")
public class WithdrawFundsController {

    private final Logger logger = Logger.getLogger(WithdrawFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    /**
     * Deposit funds into an account by passing the Account ID and command.
     * @param id Account ID
     * @param command WithdrawFundsCommand containing the quantity to withdraw
     * @return BaseResponse describing the request result
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable("id") String id,
                                                     @RequestBody WithdrawFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Withdraw funds request completed successfully."), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException exception) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to withdraw funds from bank account with for ID - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, exception);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
