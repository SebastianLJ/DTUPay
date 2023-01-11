package org.dtu.repositories;


import org.dtu.aggregate.Payment;
import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;
import org.dtu.exceptions.MerchantNotFoundException;
import org.dtu.exceptions.PaymentNotFoundException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class MerchantRepository {
    static ArrayList<User> merchants = new ArrayList<>();

    public MerchantRepository() {

    }

    public User addMerchant(String firstName, String lastName) {
        User merchant = new User(firstName, lastName);
        merchants.add(merchant);
        return merchant;
    }

    public static User getMerchant (UUID id) throws InvalidMerchantIdException {
        User targetMerchant = null;
        for (User merchant:
                merchants)  {
            if (merchant.getUserId().getUuid().equals(id)) {
                targetMerchant = merchant;
                break;
            }
        }
        return targetMerchant;
    }

    public ArrayList<User> getMerchantList() {
        return merchants;
    }

   /* public User getMerchantById(UUID id) throws PaymentNotFoundException {
        Optional<User> result = merchants.stream()
                .filter(payment -> payment.id == id)
                .findAny();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new PaymentNotFoundException("Payment with id " + id + " not found");
        }
    }*/

    public User deleteMerchant(UUID id) throws MerchantNotFoundException, InvalidMerchantIdException {
        User merchantToRemove = getMerchant(id);
        this.getMerchantList().remove(merchantToRemove);
        return merchantToRemove;
    }
}
