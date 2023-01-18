package org.dtu.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessage;
import org.dtu.event.TokensGenerated;
import org.dtu.exceptions.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Getter
public class Token implements Serializable {

    private UUID id;

    @Setter(AccessLevel.NONE)
    private List<Event> appliedEvents = new ArrayList<Event>();

    private Map<Class<? extends IDTUPayMessage>, Consumer<IDTUPayMessage>> handlers = new HashMap<>();

    public ArrayList<Token> generateTokens(UserId userid, int amount) throws InvalidTokenAmountException, InvalidTokenAmountRequestException {

        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0 ; i < amount ; i++){
            Token token = new Token();
            //tokenRepository.put(token,userid);
            tokens.add(token);
        }
        //tokenAmountRepository.put(userid, tokensAmount + tokens.size());
        return tokens;
    }

    public UserId consumeToken(Token token) throws TokenDoesNotExistException, TokenHasAlreadyBeenUsedException, NoMoreValidTokensException {
        //if (usedTokenRepository.containsKey(token)) throw new TokenHasAlreadyBeenUsedException();
        //UserId user = tokenRepository.get(token);
        //if (user == null) throw new TokenDoesNotExistException();
        //if (tokenAmountRepository.get(user) == 0) throw new NoMoreValidTokensException();
        //usedTokenRepository.put(token, user);
        //tokenRepository.remove(token);
        //tokenAmountRepository.put(user, tokenAmountRepository.get(user) - 1);
        return null; //user;
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
        handlers.put(TokensGenerated.class, e -> apply((TokensGenerated) e));
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

    private void apply(TokensGenerated event) {
        //id = event.getToken().id;
    }

    public void clearAppliedEvents() {
        appliedEvents.clear();
    }


}
