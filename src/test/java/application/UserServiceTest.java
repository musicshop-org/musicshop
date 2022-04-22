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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void initMockAndService() throws RemoteException {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void given_adminuser_when_getalltopics_then_expectalltopics() throws RemoteException {

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
    void given_useregartnerf_when_getsubscribedtopicsforuser_then_expectsubscribedtopicsforuseregartnerf() throws RemoteException {

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
    void given_userprescherm_when_getsubscribedtopicsforuser_then_expectsubscribedtopicsforuserprescherm() throws RemoteException {

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
    void given_notexistinguser_when_getsubscribedtopicsforuser_then_expectemptylist() throws RemoteException {

        // given
        String username = "notexistinguser";
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(null));

        // when
        List<String> actualTopics = userService.getSubscribedTopicsForUser(username);

        // then
        assertTrue(actualTopics.isEmpty());
    }

    @Test
    void given_existinguser_when_subscribe_then_expecttrue() throws RemoteException {

        // given
        ArrayList<Topic> topics = new ArrayList<>();
        topics.addAll(List.of(new Topic("system"), new Topic("order")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

        // when
        boolean success = userService.subscribe("someTopic", user.getUsername());

        // then
        assertTrue(success);
        assertEquals(3, topics.size());
        assertEquals("someTopic", topics.get(2).getName());
    }

    @Test
    void given_notexistinguser_when_subscribe_then_expectfalse() throws RemoteException {

        // given
        ArrayList<Topic> topics = new ArrayList<>();
        topics.addAll(List.of(new Topic("system"), new Topic("order")));
        User user = new User("notExisting", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(null));

        // when
        boolean success = userService.subscribe("someTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(2, topics.size());
    }

    @Test
    void given_existinguser_when_unsubscribe_then_expecttrue() throws RemoteException {

        // given
        ArrayList<Topic> topics = new ArrayList<>();
        topics.addAll(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));

        // when
        boolean success = userService.unsubscribe("someTopic", user.getUsername());

        // then
        assertTrue(success);
        assertEquals(2, topics.size());
    }

    @Test
    void given_notexistinguser_when_unsubscribe_then_expectfalse() throws RemoteException {

        // given
        ArrayList<Topic> topics = new ArrayList<>();
        topics.addAll(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("notExisting", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(null));

        // when
        boolean success = userService.unsubscribe("someTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(3, topics.size());
    }

    @Test
    void given_notexistingtopic_when_unsubscribe_then_expectfalse() throws RemoteException {

        // given
        ArrayList<Topic> topics = new ArrayList<>();
        topics.addAll(List.of(new Topic("system"), new Topic("order"), new Topic("someTopic")));
        User user = new User("admin", topics);

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.ofNullable(null));

        // when
        boolean success = userService.unsubscribe("notExistingTopic", user.getUsername());

        // then
        assertFalse(success);
        assertEquals(3, topics.size());
    }
}
