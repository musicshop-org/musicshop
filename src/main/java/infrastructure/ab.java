private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

@Override
public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.openSession();
        User user = session.createQuery("from User where username = lower(:username)", User.class)
        .setParameter("username", username)
        .getSingleResultOrNull();
        return Optional.ofNullable(user);