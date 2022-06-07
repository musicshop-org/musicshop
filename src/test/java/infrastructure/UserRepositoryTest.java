package infrastructure;

import domain.User;
import domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void initService() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void given_adminUser_when_findUserByUserName_then_expectAdminUser() {
        // given
        String username = "admin";

        // when
        Optional<User> actualUser = userRepository.findUserByUsername(username);

        // then
        assertEquals(username, actualUser.get().getUsername());
    }

    @Test
    void given_notExistingUser_when_findUserByUserName_then_emptyOptional() {
        // given
        String username = "notexisting";

        // when
        Optional<User> actualUser = userRepository.findUserByUsername(username);

        // then
        assertTrue(actualUser.isEmpty());
    }

    @Test
    void given_user_when_updateUser_then_changeUser() {
        // given
        LocalDateTime expectedLastViewed = LocalDateTime.now();

        String username = "meierm";
        User user = userRepository.findUserByUsername(username).get();
        user.setLastViewed(expectedLastViewed);

        // when
        userRepository.updateUser(user);

        // then
        assertEquals(expectedLastViewed.toEpochSecond(ZoneOffset.UTC), userRepository.findUserByUsername(username).get().getLastViewed().toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    void given_topic_when_deleteTopic_then_removeTopic() {
        // TODO: implement
        // given


        // when


        // then

    }
}
