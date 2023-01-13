package org.dtu.repository;

import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventStore {

    private IDTUPayMessageQueue messageQueue;

    private Map<Token, List<Event>> store = new ConcurrentHashMap<>();

    public EventStore(IDTUPayMessageQueue messageQueue){
        this.messageQueue = messageQueue;
    }

    public void addEvent(Token token, Event event){
    }
}
