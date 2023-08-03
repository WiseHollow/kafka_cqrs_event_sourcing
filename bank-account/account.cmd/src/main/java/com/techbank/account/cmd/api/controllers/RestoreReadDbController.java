package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.RestoreReadDbCommand;
import com.techbank.account.common.dtos.BaseResponse;
import com.techbank.cqrs.core.infastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/restoreReadDb")
public class RestoreReadDbController {

    private final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    /**
     * Restore the state of the database from recorded events in the Event Store.
     * @return BaseResponse describing the result of the request
     */
    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb() {
        try {
            commandDispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(new BaseResponse("Read Database Restore request completed successfully!"), HttpStatus.OK);
        } catch (IllegalStateException exception) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var safeErrorMessage = "Error while processing request to rebuild Read Database.";
            logger.log(Level.SEVERE, safeErrorMessage, exception);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
