package domain.repositories;

import domain.Topic;
import domain.User;

import java.io.Serializable;
import java.util.Optional;

public interface UserRepository extends Serializable {

    Optional<User> findUserByUsername(String username);

    void updateUser(User user);

    void deleteTopic(Topic topic);
}
