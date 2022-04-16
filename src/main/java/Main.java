
import domain.User;
import domain.repositories.UserRepository;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {

//        try {
//            RMIControllerFactory rmiControllerFactory = RMIControllerFactoryImpl.getInstance();
//            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
//            Naming.rebind("rmi://localhost/RMIControllerFactory", rmiControllerFactory);
//
//            System.out.println("Listening on port " + Registry.REGISTRY_PORT);
//
//        } catch (RemoteException | MalformedURLException e) {
//            e.printStackTrace();
//        }

//        User user = new User("admin", List.of(new Topic("system"), new Topic("order")));
//
//        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        session.persist(user);
//        transaction.commit();
//        session.close();

        UserRepository userRepository = new UserRepositoryImpl();
        Optional<User> user = userRepository.findUserByUsername("admin");
        System.out.println(user.get());

    }
}
