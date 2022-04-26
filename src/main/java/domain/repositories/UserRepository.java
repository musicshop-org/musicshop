package domain.repositories;

import domain.Topic;
import domain.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findUserByUsername(String username);

    void updateUser(User user);

    void deleteTopic(Topic topic);
}
