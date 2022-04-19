
import communication.rmi.RMIControllerFactoryImpl;
import domain.User;
import domain.repositories.ProductRepository;
import domain.repositories.UserRepository;
import infrastructure.ProductRepositoryImpl;
import infrastructure.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import sharedrmi.communication.rmi.RMIControllerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        ProductRepository productRepository = new ProductRepositoryImpl();

        productRepository.findAlbumsBySongTitle("bad").forEach(album -> System.out.println(album.getTitle()));

        try {
            System.setProperty("java.rmi.server.hostname", "10.0.40.162");
            System.setSecurityManager(new SecurityManager());

            
            RMIControllerFactory rmiControllerFactory = RMIControllerFactoryImpl.getInstance();
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("rmi://localhost/RMIControllerFactory", rmiControllerFactory);

            System.out.println("Listening on port " + Registry.REGISTRY_PORT);

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

//        User user = new User("admin", List.of(new Topic("system"), new Topic("order")));
//
//        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        session.persist(user);
//        transaction.commit();
//        session.close();

//        UserRepository userRepository = new UserRepositoryImpl();
//        Optional<User> user = userRepository.findUserByUsername("admin");
//        System.out.println(user.get());
    }
}
