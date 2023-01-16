package org.dtu.baeldung.aggregates;

import org.dtu.baeldung.commands.ConsumeTokenCommand;
import org.dtu.baeldung.commands.GenerateTokenCommand;
import org.dtu.baeldung.model.Token;
import org.dtu.baeldung.repositories.TokenWriteRepository;

public class TokenAggregate {
    private TokenWriteRepository tokenWriteRepository;

    public TokenAggregate(TokenWriteRepository tokenWriteRepository) {
        this.tokenWriteRepository = tokenWriteRepository;
    }

    public void handleGenerateTokenCommand(GenerateTokenCommand generateTokenCommand){
        for (int i = 0; i < generateTokenCommand.getAmount(); i++) {
            Token token = new Token();
            tokenWriteRepository.addToken(generateTokenCommand.getUserId(), token);
        }
    }

    public void handleConsumeTokenCommand(ConsumeTokenCommand consumeTokenCommand){
        tokenWriteRepository.consumeToken(consumeTokenCommand.getToken());
    }
}
