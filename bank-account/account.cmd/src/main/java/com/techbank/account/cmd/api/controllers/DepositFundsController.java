package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
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
@RequestMapping(path = "/api/v1/depositFunds")
public class DepositFundsController {

    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    /**
     * Deposit funds into an account by passing in the account ID and Command.
     * @param id Account ID
     * @param command DepositFundsCommand containing the quantity to deposit
     * @return BaseResponse describing the request result
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable("id") String id,
                                                     @RequestBody DepositFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Deposit funds request completed successfully."), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException exception) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to deposit funds to bank account with for ID - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, exception);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
