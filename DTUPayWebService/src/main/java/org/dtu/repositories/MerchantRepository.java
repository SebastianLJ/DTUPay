package org.dtu.repositories;

import org.dtu.InvalidMerchantIdException;
import org.dtu.MerchantAlreadyExistsException;
import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;

import java.util.ArrayList;
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
}
