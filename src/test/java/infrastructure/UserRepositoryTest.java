package infrastructure;

import domain.User;
import domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String username = "admin";

        // when
        Optional<User> actualUser = userRepository.findUserByUsername(username);

        // then
        assertEquals(username, actualUser.get().getUsername());
    }

    @Test
    void given_useregartnerf_when_getsubscribedtopicsforuser_then_expectsubscribedtopics() {

        // given
        String username = "egartnerf";

        // when
        Optional<User> actualUser = userRepository.findUserByUsername(username);

        // then
        assertEquals(username, actualUser.get().getUsername());
    }
}
