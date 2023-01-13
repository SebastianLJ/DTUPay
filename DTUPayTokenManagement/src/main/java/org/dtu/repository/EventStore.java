package org.dtu.repository;

import lombok.NonNull;
import messageUtilities.events.Event;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.TokenId;
import org.dtu.aggregate.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class EventStore {

    private IDTUPayMessageQueue messageQueue;

    private Map<TokenId, List<Event>> store = new ConcurrentHashMap<>();

    public EventStore(IDTUPayMessageQueue messageQueue){
        this.messageQueue = messageQueue;
    }

    public void addEvent(TokenId tokenid, Event event) {
        if (!store.containsKey(tokenid)) {
            store.put(tokenid, new ArrayList<Event>());
        }
        store.get(tokenid).add(event);
        messageQueue.publish(event);
    }

    public Stream<Event> getEventsFor(TokenId tokenId) {
        if (!store.containsKey(tokenId)) {
            store.put(tokenId, new ArrayList<Event>());
        }
        return store.get(tokenId).stream();
    }

    public void addEvents(@NonNull TokenId tokenId, List<Event> appliedEvents) {
        appliedEvents.stream().forEach(e -> addEvent(tokenId, e));
    }
}
