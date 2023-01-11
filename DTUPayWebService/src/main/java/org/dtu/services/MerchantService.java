package org.dtu.services;


import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;
import org.dtu.repositories.MerchantRepository;

import java.util.ArrayList;
import java.util.UUID;

public class MerchantService {
    MerchantRepository repository;

    public MerchantService() {repository = new MerchantRepository();}

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
        try {
             return repository.getMerchant(id);
        } catch (InvalidMerchantIdException e) {
            throw new InvalidMerchantIdException();
        }
    }

    public User addMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        User merchant = repository.addMerchant(firstName, lastName);
        return merchant;
    }

}
