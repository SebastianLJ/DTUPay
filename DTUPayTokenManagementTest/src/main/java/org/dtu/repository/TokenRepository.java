package org.dtu.repository;

import messageUtilities.eventSource.IEventRepository;
import messageUtilities.queues.IDTUPayMessageQueue;
import org.dtu.aggregate.Token;
import org.dtu.aggregate.TokenId;

public class TokenRepository implements IEventRepository<Token, TokenId> {

    private EventStore eventStore;

    public TokenRepository(IDTUPayMessageQueue bus) {
        eventStore = new EventStore(bus);
    }

    @Override
    public Token getByID(TokenId tokenId) {
        return Token.createFromEvents(eventStore.getEventsFor(tokenId));
    }

    @Override
    public void save(Token token) {
        eventStore.addEvents(new TokenId(token.getId()),token.getAppliedEvents());
        token.clearAppliedEvents();
    }

}
