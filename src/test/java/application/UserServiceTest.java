package application;

import domain.Album;
import domain.Topic;
import domain.User;
import domain.repositories.ProductRepository;
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
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashSet;
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
    void initService() throws RemoteException {
        userService = new UserServiceImpl();
    }

    @Test
    void given_adminuser_when_getalltopics_then_expectalltopics() throws RemoteException {

        // given
        List<Topic> expectedTopics = List.of(new Topic("system"), new Topic("order"));
        String username = "admin";
        User user = new User(username, expectedTopics);

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // when
        List<String> actualTopics = userService.getAllTopics();

        // then
        for (int i = 0; i < expectedTopics.size(); i++) {
            assertEquals(actualTopics.get(i), expectedTopics.get(i).getName());
        }
    }
}
