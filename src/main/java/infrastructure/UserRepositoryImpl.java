package infrastructure;

import domain.Topic;
import domain.User;
import domain.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.openSession();

        User user = session.createQuery("from User where username = lower(:username)", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }

    @Override
    public void updateUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteTopic(Topic topic) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(topic);
        transaction.commit();
        session.close();
    }
}
