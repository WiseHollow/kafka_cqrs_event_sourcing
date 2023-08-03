package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {

    @Getter
    protected String id;

    @Getter
    @Setter
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public List<BaseEvent> getUncommittedChanges() {
        return changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    /**
     * Using Reflection, the extending Aggregate's `apply` function will be called and have the BaseEvent instance
     * passed in. Finally, if this is a new event (not being applied for rebuild of Read Database), add the change
     * to the `changes` collection.
     * @param event BaseEvent being raised
     * @param isNewEvent True if we want to announce this event with Kafka
     */
    protected void applyChanges(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException | IllegalArgumentException exception) {
            logger.log(Level.WARNING, MessageFormat.format("The apply method was not found in the aggregate for {0}", event.getClass().getName()));
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Error applying event to aggregate", exception);
        } finally {
            if (isNewEvent) {
                changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChanges(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChanges(event, false));
    }
}
