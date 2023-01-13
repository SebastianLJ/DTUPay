package org.dtu.repositories;

import messageUtilities.eventSource.IEventRepository;
import org.dtu.aggregate.User;
import org.dtu.aggregate.UserId;

public class UserRepository implements IEventRepository<User, UserId> {

    @Override
    public User getByID(UserId obj) {
        return null;
    }

    @Override
    public void save(User obj) {

    }
}
