package com.techbank.cqrs.core.infastructure;

import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {

    /**
     * Associate a BaseCommand class type with a FunctionalInterface CommandHandlerMethod.
     * @param type Class type
     * @param handler CommandHandlerMethod used for this type of command
     * @param <T> Type must extend BaseCommand
     */
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);

    /**
     * The correct and registered `CommandHandlerMethod` will be retrieved and used to handle the passed in BaseCommand.
     * @param command Command to send to its CommandHandlerMethod
     */
    void send(BaseCommand command);
}
