
import communication.rest.JwtManager;
import communication.rest.RestLoginServiceImpl;
import communication.rest.api.RestLoginService;

import java.rmi.registry.Registry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

//        // customer
//        String emailAddress = "AdelinaLandmann1987@email.test";
//        String pw = "password04";
//
//        // licensee
//        String emailAddress2 = "CharlieBittner2006@email.test";
//        String pw2 = "password03";
//
//        // employee
//        String emailAddress3 = "essiga";
//        String pw3 = "password01";
//
//        RestLoginService restLoginService = new RestLoginServiceImpl();
//        restLoginService.checkCredentials(emailAddress, pw);
//        restLoginService.getRole(emailAddress3);


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

            String jwt = JwtManager.createJWT("Test", 900000);
            boolean notOkay = JwtManager.validateJwt("abc");
            boolean notOkay2 = JwtManager.validateJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
            boolean okay = JwtManager.validateJwt(jwt);

            System.out.println(notOkay);
            System.out.println(notOkay2);
            System.out.println(okay);

//        // create JWT
//        String emailAddress = "AdelinaLandmann1987@email.test";
//        String pw = "password04";
//
//        String emailAddress2 = "CharlieBittner2006@email.test";
//        String pw2 = "password03";
//
//        String username = "Test_Customer";
//
//        long expiration = 900000;
//        String jwtToken = JwtManager.createJWT(emailAddress2, expiration);
//
//        System.out.println("Generated_Token:" + jwtToken);
//
//        System.out.println(JwtManager.getId(jwtToken));
//        System.out.println(JwtManager.getIssuedAt(jwtToken));
//        System.out.println(JwtManager.getExpiration(jwtToken));
//        System.out.println(JwtManager.getIssuer(jwtToken));
//        System.out.println(JwtManager.getEmailAddress(jwtToken));
//        System.out.println(JwtManager.getRoles(jwtToken));
//
//        // decode JWT
//        System.out.println(JwtManager.decodeJWT(jwtToken));
    }
}
