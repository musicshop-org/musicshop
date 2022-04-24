package application;

import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import infrastructure.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import sharedrmi.application.api.UserService;
import sharedrmi.application.exceptions.UserNotFoundException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    private final UserRepository userRepository;

    protected UserServiceImpl() throws RemoteException {
        super();
        this.userRepository = new UserRepositoryImpl();
    }

    @Transactional
    @Override
    public List<String> getAllTopics() throws RemoteException {
        Optional<User> userOpt = userRepository.findUserByUsername("admin");

        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }

        List<Topic> topics = userOpt.get().getTopics();

        List<String> topicNames = new ArrayList<>();

        for (Topic topic : topics) {
            topicNames.add(topic.getName());
        }

        return topicNames;
    }

    @Transactional
    @Override
    public List<String> getSubscribedTopicsForUser(String username) throws RemoteException {
        Optional<User> userOpt = userRepository.findUserByUsername(username);

        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }

        List<Topic> topics = userOpt.get().getTopics();

        List<String> topicNames = new ArrayList<>();

        for (Topic topic : topics) {
            topicNames.add(topic.getName());
        }

        return topicNames;
    }

    @Transactional
    @Override
    public void changeLastViewed(String username, LocalDateTime lastViewed) throws UserNotFoundException {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setLastViewed(lastViewed);
            userRepository.updateUser(user);
        } else {
            throw new UserNotFoundException("User with "+ username + " not found");
        }
    }
}
