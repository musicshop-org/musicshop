package application;

import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import infrastructure.UserRepositoryImpl;
import sharedrmi.application.api.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

    @Override
    public void subscribe(String topic, String username) throws RemoteException {
        Optional<User> optUser = userRepository.findUserByUsername(username);
        User user = optUser.get();

        List<Topic> topics = user.getTopics();
        topics.add(new Topic(topic));

        userRepository.updateUser(user);
    }

    @Override
    public void unsubscribe(String topic, String username) throws RemoteException {
        Optional<User> optUser = userRepository.findUserByUsername(username);
        User user = optUser.get();

        List<Topic> topics = user.getTopics();

        int indexToRemove = -1;

        for (int i = 0; i < topics.size(); i++) {

            if (topics.get(i).getName().equals(topic)) {
                indexToRemove = i;
                break;
            }
        }

        topics.remove(indexToRemove);

        userRepository.updateUser(user);
    }
}
