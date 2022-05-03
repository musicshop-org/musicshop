
import communication.rest.JwtManager;

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

        // create JWT
        String username = "Test_Customer";
        long expiration = 900000;
        String jwtToken = JwtManager.createJWT(username, expiration);

        System.out.println("Generated_Token:" + jwtToken);

        System.out.println(JwtManager.getId(jwtToken));
        System.out.println(JwtManager.getIssuedAt(jwtToken));
        System.out.println(JwtManager.getExpiration(jwtToken));
        System.out.println(JwtManager.getIssuer(jwtToken));
        System.out.println(JwtManager.getUsername(jwtToken));



        // decode JWT
        System.out.println(JwtManager.decodeJWT(jwtToken));
    }
}
