
import communication.rest.JwtManager;
import communication.rest.RestLoginServiceImpl;
import communication.rest.api.RestLoginService;

import java.rmi.registry.Registry;
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
    }
}
