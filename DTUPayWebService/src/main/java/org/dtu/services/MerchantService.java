package org.dtu.services;


import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import org.dtu.aggregate.User;
import org.dtu.exceptions.*;
import org.dtu.repositories.MerchantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MerchantService {
    MerchantRepository repository;

    public MerchantService(IDTUPayMessageQueue dtuPayRabbitMQ) {repository = new MerchantRepository();}

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
        try {
            User newUser = repository.addMerchant(firstName, lastName);
            return newUser;
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }

    }

    public User addMerchant(String firstName, String lastName, String bankAccount) throws MerchantAlreadyExistsException {
        try {
            User newUser = repository.addMerchant(firstName, lastName, bankAccount);
            return newUser;
        } catch (MerchantAlreadyExistsException e) {
            throw new MerchantAlreadyExistsException();
        }
    }


    public ArrayList<User> getMerchantList() {
        return repository.getMerchantList();
    }

    public User deleteMerchant(UUID id) throws PaymentNotFoundException, MerchantNotFoundException, InvalidMerchantIdException {
        return repository.deleteMerchant(id);
    }

}
