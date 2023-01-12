package org.dtu.services;


import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;
import org.dtu.exceptions.MerchantNotFoundException;
import org.dtu.exceptions.PaymentNotFoundException;
import org.dtu.repositories.MerchantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MerchantService {
    MerchantRepository repository;

    public MerchantService() {repository = new MerchantRepository();}

    public List<User> getMerchants() {
        return repository.getMerchantList();
    }

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
        try {
             return MerchantRepository.getMerchant(id);
        } catch (InvalidMerchantIdException e) {
            throw new InvalidMerchantIdException();
        }
    }

    public User addMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        User merchant = repository.addMerchant(firstName, lastName);
        return merchant;
    }

    public User addMerchant(String firstName, String lastName, String bankAccount) throws MerchantAlreadyExistsException {
        User merchant = repository.addMerchant(firstName, lastName, bankAccount);
        return merchant;
    }


    public ArrayList<User> getMerchantList() {
        return repository.getMerchantList();
    }

    public User deleteMerchant(UUID id) throws PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        return repository.deleteMerchant(id);
    }

}
