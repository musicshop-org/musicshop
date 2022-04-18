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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    private UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void initService() throws RemoteException {
        userService = new UserServiceImpl();
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
}
