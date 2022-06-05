package application;

import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import sharedrmi.application.api.UserService;
import sharedrmi.application.exceptions.UserNotFoundException;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private static UserRepository userRepository;

    @BeforeEach
    void initMockAndService() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void given_adminUser_when_getAllTopics_then_expectAllTopics() {
        // given
        String username = "admin";
        List<Topic> expectedTopics = List.of(new Topic("system"), new Topic("order"));
        User user = new User(username, expectedTopics);

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // when
        List<String> actualTopics = userService.getAllTopics();

        // then
        for (int i = 0; i < expectedTopics.size(); i++) {
            assertEquals(actualTopics.get(i), expectedTopics.get(i).getName());
        }
    }

    @Test
    void given_user_egartnerf_when_getSubscribedTopicsForUser_then_expectSubscribedTopicsForUser() {
        // given
        String username = "egartnerf";
        List<Topic> expectedTopics = List.of(new Topic("system"), new Topic("order"));
        User user = new User(username, expectedTopics);

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // when
        List<String> actualTopics = userService.getSubscribedTopicsForUser(username);

        // then
        for (int i = 0; i < expectedTopics.size(); i++) {
            assertEquals(actualTopics.get(i), expectedTopics.get(i).getName());
        }
    }

    @Test
    void given_user_prescherm_when_getSubscribedTopicsForUser_then_expectSubscribedTopicsForUser() {
        // given
        String username = "prescherm";
        List<Topic> expectedTopics = List.of(new Topic("system"));
        User user = new User(username, expectedTopics);

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // when
        List<String> actualTopics = userService.getSubscribedTopicsForUser(username);

        // then
        for (int i = 0; i < expectedTopics.size(); i++) {
            assertEquals(actualTopics.get(i), expectedTopics.get(i).getName());
        }
    }

    @Test
    void given_notExistingUser_when_getSubscribedTopicsForUser_then_expectEmptyLst() {
        // given
        String username = "notexistinguser";
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        // when
        List<String> actualTopics = userService.getSubscribedTopicsForUser(username);

        // then
        assertTrue(actualTopics.isEmpty());
    }

    @Test
    void given_userNameAndLastViewed_when_changeLastViewed_then_changeLastViewedOfUser() throws UserNotFoundException {
        // given
        String username = "meierm";
        User user = new User(username, new ArrayList<>());
        LocalDateTime expectedLastViewedTime = LocalDateTime.now();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // when
        userService.changeLastViewed(username, expectedLastViewedTime);

        // then
        assertEquals(expectedLastViewedTime, user.getLastViewed());
    }

    @Test
    void given_notExistingUserName_when_changeLastViewed_then_changeLastViewedOfUser() {
        // given
        String username = "notexistinguser";

        // when ... then
        assertThrows(UserNotFoundException.class, () -> userService.changeLastViewed(username, LocalDateTime.now()));
    }

    @Test
    void given_userName_when_getLastViewedForUser_then_returnTimeLastViewed() throws UserNotFoundException {
        // given
        String username = "meierm";
        User user = new User(username, new ArrayList<>());
        LocalDateTime expectedLastViewedTime = LocalDateTime.now();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        user.setLastViewed(expectedLastViewedTime);

        // when
        LocalDateTime actualLastViewedTime = userService.getLastViewedForUser(username);

        // then
        assertEquals(expectedLastViewedTime, actualLastViewedTime);
    }

    @Test
    void given_notExistingUserName_when_getLastViewedForUser_then_returnTimeLastViewed() {
        // given
        String username = "notexistinguser";

        // when ... then
        assertThrows(UserNotFoundException.class, () -> userService.getLastViewedForUser(username));
    }

    @Test
    void given_existingUser_when_subscribe_then_expectTrue() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // when
        boolean success = userService.subscribe("someTopic", user.getUsername());

        // then
        assertTrue(success);
        assertEquals(3, topics.size());
        assertEquals("someTopic", topics.get(2).getName());
    }

    @Test
    void given_notExistingUser_when_subscribe_then_expectFalse() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order")));
        User user = new User("notExisting", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());

        // when
        boolean success = userService.subscribe("someTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(2, topics.size());
    }

    @Test
    void given_existingUser_when_unsubscribe_then_expectTrue() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // when
        boolean success = userService.unsubscribe("someTopic", user.getUsername());

        // then
        assertTrue(success);
        assertEquals(2, topics.size());
    }

    @Test
    void given_existingUser_when_unsubscribeNotExistingTopic_then_expectFalse() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // when
        boolean success = userService.unsubscribe("someTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(2, topics.size());
    }

    @Test
    void given_notExistingUser_when_unsubscribe_then_expectFalse() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("notExisting", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());

        // when
        boolean success = userService.unsubscribe("someTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(3, topics.size());
    }

    @Test
    void given_notExistingTopic_when_unsubscribe_then_expectFalse() {
        // given
        ArrayList<Topic> topics = new ArrayList<>(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());

        // when
        boolean success = userService.unsubscribe("notExistingTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(3, topics.size());
    }
}
