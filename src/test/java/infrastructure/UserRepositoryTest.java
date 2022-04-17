package infrastructure;

import application.UserServiceImpl;
import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sharedrmi.application.api.UserService;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void initService() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void given_adminuser_when_finduserbyusername_then_expectadminuser() {

        // given
        List<Topic> expectedTopics = List.of(new Topic("system"), new Topic("order"));
        String username = "admin";
        User user = new User(username, expectedTopics);

        // when
        Optional<User> actualUser = userRepository.findUserByUsername(username);

        // then
        assertEquals(username, actualUser.get().getUsername());

        List<Topic> actualTopics = actualUser.get().getTopics();

        for (int i = 0; i < expectedTopics.size(); i++) {
            assertEquals(actualTopics.get(i).getName(), expectedTopics.get(i).getName());
        }
    }
}
