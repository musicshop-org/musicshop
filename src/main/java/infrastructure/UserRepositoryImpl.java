package infrastructure;

import domain.User;
import domain.repositories.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.empty();
    }
    //teet
}
