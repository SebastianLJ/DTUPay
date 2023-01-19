package org.dtu.repositories;

import org.dtu.aggregate.User;
import org.dtu.exceptions.InvalidMerchantIdException;
import org.dtu.exceptions.MerchantAlreadyExistsException;

import java.util.ArrayList;
import java.util.UUID;

public class MerchantRepository {

    private final ArrayList<User> merchants = new ArrayList<>();

    public MerchantRepository() {

    }

    public User addMerchant(String firstName, String lastName) throws MerchantAlreadyExistsException {
        User merchant = new User(firstName, lastName);
        merchants.add(merchant);
        return merchant;
    }

    public User addMerchant(String firstName, String lastName, String bankAccount) throws MerchantAlreadyExistsException {
        User merchant = new User(firstName, lastName, bankAccount);
        merchants.add(merchant);
        return merchant;
    }

    public User addMerchant(User user) throws MerchantAlreadyExistsException {
        User merchant = new User(user.getName().getFirstName(), user.getName().getLastName(), user.getBankNumber());
        merchants.add(merchant);
        return merchant;
    }

    public User getMerchant (UUID id) throws InvalidMerchantIdException {
        for (User merchant:
                merchants)  {
            if (merchant.getUserId().getUuid().equals(id)) {
                return merchant;
            }
        }
        throw new InvalidMerchantIdException();
    }

    public ArrayList<User> getMerchantList() {
        return merchants;
    }

    public User deleteMerchant(UUID id) throws InvalidMerchantIdException {
        User merchantToRemove = getMerchant(id);
        this.merchants.remove(merchantToRemove);
        return merchantToRemove;
    }
}
