package org.dtu;

import org.dtu.aggregate.User;

import java.util.ArrayList;
import java.util.UUID;

public class MerchantService {
    static ArrayList<User> merchants = new ArrayList<>();
    public ArrayList<User> getMerchants() {
        return merchants;
    }

    public MerchantService() {

    }

    public static User getMerchant (UUID id) throws InvalidMerchantIdException {
        for (User merchant:
                merchants)  {
            if (merchant.getUserId().getUuid().equals(id)) {
                return merchant;
            }
        }
        throw new InvalidMerchantIdException();
    }

    public void addMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        // if merchant already exists, throw MerchantAlreadyExists exception
        User merchant = new User(firstName, lastName);
        merchants.add(merchant);
    }

}
