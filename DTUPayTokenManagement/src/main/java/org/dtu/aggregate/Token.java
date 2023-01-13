package org.dtu.aggregate;

import lombok.*;
import messageUtilities.events.Event;
import messageUtilities.queues.IDTUPayMessage;
import org.dtu.event.GeneratedToken;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Getter
public class Token {

    private UUID id;

    @Setter(AccessLevel.NONE)
    private List<Event> appliedEvents = new ArrayList<Event>();

    private Map<Class<? extends IDTUPayMessage>, Consumer<IDTUPayMessage>> handlers = new HashMap<>();

    public static Token create(){
        Token token = new Token();
        GeneratedToken generatedToken = new GeneratedToken(token);
        token.appliedEvents.add(generatedToken);
        return token;
    }

    public static Token createFromEvents(Stream<Event> events) {
        Token token = new Token();
        token.applyEvents(events);
        return token;
    }

    public Token() {
        this.id = UUID.randomUUID();
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        handlers.put(GeneratedToken.class, e -> apply((GeneratedToken) e));
    }

    private void applyEvents(Stream<Event> events) throws Error {
        events.forEachOrdered(e -> {
            this.applyEvent(e);
        });
        if (this.getId() == null) {
            throw new Error("token does not exist");
        }
    }

    private void applyEvent(Event e) {
        handlers.getOrDefault(e.getClass(), this::missingHandler).accept(e);
    }

    private void missingHandler(IDTUPayMessage e) {
        throw new Error("handler for event "+e+" missing");
    }

    private void apply(GeneratedToken event) {
        id = event.getToken().id;
    }

    public void clearAppliedEvents() {
        appliedEvents.clear();
    }


}
