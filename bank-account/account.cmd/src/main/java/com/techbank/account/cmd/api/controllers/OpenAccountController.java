package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dtos.BaseResponse;
import com.techbank.cqrs.core.infastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {

    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    /**
     * Open a new account
     * @param command OpenAccountCommand describing the structure of the new account
     * @return BaseResponse describing the request result
     */
    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);

        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Bank account creation request completed successfully.", id), HttpStatus.CREATED);
        } catch (IllegalStateException exception) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to open a new bank account for ID - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, exception);
            return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
