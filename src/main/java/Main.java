
import communication.rest.JWTManager;
import communication.rmi.RMIControllerFactoryImpl;
import domain.User;
import domain.repositories.ProductRepository;
import domain.repositories.UserRepository;
import infrastructure.ProductRepositoryImpl;
import infrastructure.UserRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import sharedrmi.communication.rmi.RMIControllerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

//        try {
//            System.setProperty("java.rmi.server.hostname", "localhost");
//            //System.setSecurityManager(new SecurityManager());
//
//
//            RMIControllerFactory rmiControllerFactory = RMIControllerFactoryImpl.getInstance();
//            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
//            Naming.rebind("rmi://localhost/RMIControllerFactory", rmiControllerFactory);
//
//            System.out.println("Listening on port " + Registry.REGISTRY_PORT);
//
//        } catch (RemoteException | MalformedURLException e) {
//            e.printStackTrace();
//        }

        // create JWT
        UUID id = UUID.randomUUID();
        String issuer = "Musicshop";
        String subject = "Test_Customer";
        String jwtToken = JWTManager.createJWT(id, issuer, subject, TimeUnit.SECONDS.toMillis(900));

        System.out.println("Generated_Token:" + jwtToken);

        // decode JWT
        System.out.println(JWTManager.decodeJWT(jwtToken));
        System.out.println("Listening on port " + Registry.REGISTRY_PORT);

    }
}
