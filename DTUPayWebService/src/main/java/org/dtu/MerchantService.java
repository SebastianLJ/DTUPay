package org.dtu;

import org.dtu.resources.User;

import java.util.ArrayList;

public class MerchantService {
    static ArrayList<User> merchants = new ArrayList<>();
    public ArrayList<User> getMerchants() {
        return merchants;
    }

    public MerchantService() {

    }

    public static User getMerchant (String id) throws InvalidMerchantIdException {
        for (User merchant:
                merchants)  {
            if (merchant.getId().equals(id)) {
                return merchant;
            }
        }
        throw new InvalidMerchantIdException();
    }

    public void addMerchant(String id) throws MerchantAlreadyExistsException {
        // if merchant already exists, throw MerchantAlreadyExists exception
        try {
            getMerchant(id);
            throw new MerchantAlreadyExistsException();
        } catch (InvalidMerchantIdException e) {
            User merchant = new User(id);
            merchants.add(merchant);
        }
    }

}
