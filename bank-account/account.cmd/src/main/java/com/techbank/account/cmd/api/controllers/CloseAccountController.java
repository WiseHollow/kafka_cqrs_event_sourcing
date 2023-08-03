package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
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
@RequestMapping("/api/v1/closeBankAccount")
public class CloseAccountController {

    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    /**
     * Close an account by the passed in ID.
     * @param id Account ID
     * @return BaseResponse describing the request result.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable("id") String id) {
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new BaseResponse("Bank account closure request successfully completed."), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException exception) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to close the bank account with for ID - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, exception);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
