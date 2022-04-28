package application;

import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import infrastructure.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import sharedrmi.application.api.UserService;
import sharedrmi.application.exceptions.UserNotFoundException;

import javax.ejb.Remote;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Remote(UserService.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    protected UserServiceImpl() {
        super();
        this.userRepository = new UserRepositoryImpl();
    }

    protected UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<String> getAllTopics() {
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
    public List<String> getSubscribedTopicsForUser(String username) {
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

    @Transactional
    @Override
    public LocalDateTime getLastViewedForUser(String username) throws UserNotFoundException {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            return user.getLastViewed();
        } else {
            throw new UserNotFoundException("User with "+ username + " not found");
        }
    }

    @Override
    public boolean subscribe(String topic, String username) {
        Optional<User> optUser = userRepository.findUserByUsername(username);

        if (optUser.isEmpty())
            return false;

        User user = optUser.get();
        List<Topic> topics = user.getTopics();
        topics.add(new Topic(topic));

        userRepository.updateUser(user);

        return true;
    }

    @Override
    public boolean unsubscribe(String topic, String username) {
        Optional<User> optUser = userRepository.findUserByUsername(username);

        if (optUser.isEmpty())
            return false;

        User user = optUser.get();

        List<Topic> topics = user.getTopics();
        int indexToRemove = -1;

        for (int i = 0; i < topics.size(); i++) {

            if (topics.get(i).getName().equals(topic)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1)
            return false;

        Topic topicToDelete = topics.remove(indexToRemove);

        userRepository.updateUser(user);
        userRepository.deleteTopic(topicToDelete);

        return true;
    }
}
