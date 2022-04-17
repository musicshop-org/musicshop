package infrastructure;

import domain.User;
import domain.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.openSession();

        User user = session.createQuery("from User")
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }
}
